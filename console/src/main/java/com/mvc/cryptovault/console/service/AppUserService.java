package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.bean.dto.AppUserDTO;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.AppUserRetVO;
import com.mvc.cryptovault.common.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserLogVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.common.util.InviteUtil;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.common.util.MnemonicUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.AppUserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.mvc.cryptovault.common.constant.RedisConstant.APP_USER_USERNAME;

@Service
public class AppUserService extends AbstractService<AppUser> implements BaseService<AppUser> {

    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    AppMessageService appMessageService;
    @Autowired
    AppProjectPartakeService appProjectPartakeService;
    @Autowired
    AppUserMapper appUserMapper;
    @Autowired
    AppUserInviteService appUserInviteService;
    @Autowired
    FinancialService financialService;
    @Autowired
    AppFinancialDetailService appFinancialDetailService;
    @Autowired
    AppUserFinancialPartakeService appUserFinancialPartakeService;
    @Autowired
    AppOrderService appOrderService;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    AppUserFinancialIncomeService appUserFinancialIncomeService;

    public PageInfo<DUSerVO> findUser(PageDTO pageDTO, String cellphone, Integer status) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), "id desc");
        Condition condition = new Condition(AppUser.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "cellphone = ", cellphone);
        ConditionUtil.andCondition(criteria, "status = ", status);
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        List<AppUser> list = findByCondition(condition);
        List<DUSerVO> vos = new ArrayList<>(list.size());
        for (AppUser appUser : list) {
            DUSerVO vo = new DUSerVO();
            BeanUtils.copyProperties(appUser, vo);
            List<TokenBalanceVO> data = appUserBalanceService.getAsset(appUser.getId(), true);
            BigDecimal sum = data.stream().map(obj -> obj.getRatio().multiply(obj.getValue())).reduce(BigDecimal.ZERO, BigDecimal::add);
            vo.setBalance(sum);
            vo.setInviteNum(appUser.getInviteNum());
            vo.setFinancialBalance(financialService.getFinancialBalanceSum(appUser.getId()));
            vos.add(vo);
        }
        PageInfo result = new PageInfo(list);
        result.setList(vos);
        return result;
    }

    public PageInfo<DUserLogVO> getUserLog(BigInteger id, PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderBy());
        Condition condition = new Condition(AppMessage.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", id);
        ConditionUtil.andCondition(criteria, "created_at >= ", pageDTO.getCreatedStartAt());
        ConditionUtil.andCondition(criteria, "created_at <= ", pageDTO.getCreatedStopAt());
        List<AppMessage> list = appMessageService.findByCondition(condition);
        List<DUserLogVO> vos = new ArrayList<>(list.size());
        for (AppMessage appUser : list) {
            DUserLogVO vo = new DUserLogVO();
            BeanUtils.copyProperties(appUser, vo);
            vos.add(vo);
        }
        PageInfo result = new PageInfo(list);
        result.setList(vos);
        return result;
    }

    public String getTag(BigInteger userId) {
        String tag = appProjectPartakeService.getTag(userId);
        return tag;
    }

    public AppUserRetVO register(AppUserDTO appUserDTO) {
        AppUser userTemp = findOneBy("email", appUserDTO.getEmail());
        Assert.isNull(userTemp, MessageConstants.getMsg("USER_EXIST"));
        long time = System.currentTimeMillis();
        AppUser appUser = new AppUser();
        appUser.setStatus(4);
        appUser.setUpdatedAt(time);
        appUser.setCreatedAt(time);
        appUser.setNickname(appUserDTO.getNickname());
        appUser.setInviteNum(0);
        appUser.setEmail(appUserDTO.getEmail());
        appUser.setPassword(appUserDTO.getPassword());
        appUser.setInviteLevel(0);
        appUser.setTransactionPassword(appUserDTO.getTransactionPassword());
        String code = getCode();
        appUser.setPvKey(code);
        save(appUser);
        AppUserRetVO vo = new AppUserRetVO();
        vo.setMnemonics(MnemonicUtil.getWordsList(code));
        vo.setPrivateKey(code);
        //更新邀请人数量
        Long id = InviteUtil.codeToId(appUserDTO.getInviteCode());
        appUserMapper.updateInvite(BigInteger.valueOf(id));
        //添加邀请记录
        appUserInviteService.insert(BigInteger.valueOf(id), appUser.getId());
        updateCache(appUser.getId());
        String key = APP_USER_USERNAME + appUserDTO.getEmail();
        redisTemplate.opsForHash().put(key, key, String.valueOf(appUser.getId()));
        return vo;
    }

    private String getCode() {
        String result = null;
        while (true) {
            result = MnemonicUtil.getRandomCode();
            List<AppUser> list = findBy("pvKey", result);
            if (list.size() == 0) {
                break;
            }
        }
        return result;
    }

    public void mnemonicsActive(String email) {
        AppUser appUser = findOneBy("email", email);
        appUser.setStatus(1);
        appUser.setUpdatedAt(System.currentTimeMillis());
        update(appUser);
        updateCache(appUser.getId());
    }

    public void sign(BigInteger userId) {
        //签到时自己的理财解锁,影子积分入账,上级添加奖励积分
        List<AppUserFinancialPartake> list = appUserFinancialPartakeService.findBy("userId", userId);
        for (AppUserFinancialPartake partake : list) {
            AppFinancial appFinancial = financialService.findById(partake.getFinancialId());
            BigDecimal value = (partake.getCreatedAt() + RedisConstant.ONE_DAY) < System.currentTimeMillis() ? appUserFinancialPartakeService.getIncomeDay(partake, appFinancial) : BigDecimal.ZERO;
            BigDecimal shadow = partake.getShadowValue();
            BigDecimal income = value.add(shadow);
            if (income.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            List<AppFinancialDetail> detail = appFinancialDetailService.findDetails(partake.getFinancialId());
            Collections.reverse(detail);
            if (shadow.compareTo(BigDecimal.ZERO) > 0) {
                partake.setShadowValue(BigDecimal.ZERO);
                partake.setUpdatedAt(System.currentTimeMillis());
                String messageIncome = shadow.setScale(4, RoundingMode.DOWN) + " " + commonTokenService.getTokenName(partake.getTokenId()) + appFinancial.getName() + " 奖励已发放";
                appOrderService.saveOrder(4, partake.getId(), BusinessConstant.CONTENT_FINANCIAL, getOrderNumber(), shadow, partake.getUserId(), partake.getTokenId(), 2, 1, appFinancial.getName(), messageIncome, false);
                appUserFinancialIncomeService.insert(partake, appFinancial, shadow);
            }
            if (partake.getTimes() < appFinancial.getTimes()) {
                partake.setUpdatedAt(System.currentTimeMillis());
                partake.setTimes(partake.getTimes() + 1);
                String messageLock = value.setScale(4, RoundingMode.DOWN) + " " + commonTokenService.getTokenName(partake.getTokenId()) + appFinancial.getName() + " 收益已发放";
                appOrderService.saveOrder(4, partake.getId(), BusinessConstant.CONTENT_FINANCIAL, getOrderNumber(), value, partake.getUserId(), partake.getTokenId(), 2, 1, appFinancial.getName(), messageLock, false);
            }
            appUserBalanceService.updateBalance(userId, partake.getTokenId(), income);
            appUserFinancialPartakeService.update(partake);
            for (int i = 0; i < appFinancial.getDepth(); i++) {
                BigDecimal incomeParent = income.divide(BigDecimal.valueOf(100)).multiply(new BigDecimal(detail.get(i).getRatio()));
                AppUserInvite user = appUserInviteService.findOneBy("inviteUserId", userId);
                if (null == user) {
                    break;
                }
                appUserFinancialPartakeService.addShadow(user.getUserId(), incomeParent, appFinancial.getId());
            }
        }

    }
}