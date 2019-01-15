package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.bean.dto.FinancialBuyDTO;
import com.mvc.cryptovault.common.bean.dto.FinancialPartakeDTO;
import com.mvc.cryptovault.common.bean.dto.FinancialPartakeListDTO;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.FinancialMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class FinancialService extends AbstractService<AppFinancial> implements BaseService<AppFinancial> {

    @Autowired
    FinancialMapper financialMapper;
    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    AppFinancialContentService appFinancialContentService;
    @Autowired
    AppUserFinancialPartakeService appUserFinancialPartakeService;
    @Autowired
    AppUserFinancialIncomeService appUserFinancialIncomeService;
    @Autowired
    AppOrderService appOrderService;
    @Autowired
    AppUserService appUserService;

    public Boolean unlockPartake(BigInteger id, BigInteger userId) {
        AppUserFinancialPartake partake = appUserFinancialPartakeService.findById(id);
        AppFinancial financial = findById(partake.getFinancialId());
        Assert.isTrue(!partake.getStatus().equals(3), MessageConstants.getMsg("TIME_OVER"));
        Assert.isTrue(partake.getStatus().equals(2), MessageConstants.getMsg("TIME_WRONG"));
        Assert.isTrue(partake.getUserId().equals(userId), MessageConstants.getMsg("PERMISSION_WRONG"));
        partake.setStatus(3);
        partake.setUpdatedAt(System.currentTimeMillis());
        appUserFinancialPartakeService.update(partake);
        appUserFinancialPartakeService.updateCache(id);
        appUserBalanceService.updateBalance(userId, financial.getBaseTokenId(), partake.getValue());
        appUserBalanceService.updateBalance(userId, financial.getTokenId(), partake.getIncome());
        appOrderService.saveOrder(partake, financial);
        return true;
    }

    public Boolean buyFinancial(BigInteger id, FinancialBuyDTO financialBuyDTO, BigInteger userId) {
        AppUser user = appUserService.findById(userId);
        AppFinancial financial = findById(id);
        if (null == user || null == financial) {
            return false;
        }
        Assert.isTrue(financialBuyDTO.getTransactionPassword().equals(user.getTransactionPassword()));
        Assert.isTrue(financialBuyDTO.getValue().compareTo(financial.getMinValue()) > 0, MessageConstants.getMsg("APP_TRANSACTION_MIN_OVER"));
        Assert.isTrue(financialBuyDTO.getValue().compareTo(financial.getMinValue()) > 0, MessageConstants.getMsg("APP_TRANSACTION_MIN_OVER"));
        BigDecimal balance = appUserBalanceService.getBalanceByTokenId(userId, financial.getBaseTokenId());
        Integer num = financialMapper.updateSold(financial.getId(), financialBuyDTO.getValue());
        Assert.isTrue(num == 1, MessageConstants.getMsg("PROJECT_LIMIT_OVER"));
        Assert.isTrue(balance.compareTo(financialBuyDTO.getValue()) >= 0, MessageConstants.getMsg("INSUFFICIENT_BALANCE"));
        appUserFinancialPartakeService.buy(financial, financialBuyDTO, userId);
        return true;
    }

    public List<FinancialPartakeListVO> getPartakeList(BigInteger partakeId, FinancialPartakeListDTO dto, BigInteger userId) {
        AppUserFinancialPartake partake = appUserFinancialPartakeService.findById(partakeId);
        if (null == partake) return null;
        AppFinancial financial = findById(partake.getFinancialId());
        if (null == financial) return null;
        PageHelper.startPage(1, dto.getPageSize(), "id desc");
        Condition condition = new Condition(AppUserFinancialPartake.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "partake_id = ", partakeId);
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        if (dto.getId() != null && !dto.getId().equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id < ", dto.getId());
        }
        List<AppUserFinancialIncome> list = appUserFinancialIncomeService.findByCondition(condition);
        return list.stream().map(obj -> {
            FinancialPartakeListVO vo = new FinancialPartakeListVO();
            vo.setIncome(obj.getValue());
            vo.setId(obj.getId());
            vo.setCreatedAt(obj.getCreatedAt());
            vo.setTokenName(financial.getTokenName());
            return vo;
        }).collect(Collectors.toList());
    }

    public FinancialPartakeDetailVO getPartakeDetail(BigInteger id, BigInteger userId) {
        AppUserFinancialPartake partake = appUserFinancialPartakeService.findById(id);
        if (null == partake) {
            return null;
        }
        AppFinancial appFinancial = findById(partake.getFinancialId());
        if (null == appFinancial) {
            return null;
        }
        FinancialPartakeDetailVO vo = new FinancialPartakeDetailVO();
        BeanUtils.copyProperties(appFinancial, vo);
        vo.setIncome(partake.getIncome());
        vo.setFinancialName(appFinancial.getName());
        vo.setTimes(appFinancial.getTimes() - partake.getTimes());
        vo.setValue(partake.getValue());
        return vo;
    }

    public FinancialDetailVO getFinancialDetail(BigInteger id, BigInteger userId) {
        AppFinancial appFinancial = findById(id);
        if (null == appFinancial) {
            return null;
        }
        FinancialDetailVO vo = new FinancialDetailVO();
        BeanUtils.copyProperties(appFinancial, vo);
        vo.setBalance(appUserBalanceService.getBalanceByTokenId(userId, appFinancial.getBaseTokenId()));
        BigDecimal partake = appUserFinancialPartakeService.getPartake(userId, id);
        AppFinancialContent content = appFinancialContentService.findById(id);
        vo.setPurchased(partake);
        vo.setContent(content.getContent());
        vo.setRule(content.getRule());
        return vo;
    }

    public FinancialBalanceVO getFinancialBalance(BigInteger userId) {
        FinancialBalanceVO vo = new FinancialBalanceVO();
        BigDecimal balance = appUserFinancialPartakeService.getBalance(userId);
        BigDecimal income = appUserFinancialPartakeService.getIncome(userId);
        BigDecimal lastIncome = appUserFinancialIncomeService.getLast(userId);
        vo.setBalance(balance);
        vo.setIncome(income);
        vo.setLastIncome(lastIncome);
        return vo;
    }

    public List<FinancialUserPartakeVO> getFinancialPartakeList(FinancialPartakeDTO financialPartakeDTO, BigInteger userId) {
        Condition condition = new Condition(AppUserFinancialPartake.class);
        Example.Criteria criteria = condition.createCriteria();
        PageHelper.startPage(1, financialPartakeDTO.getPageSize(), "created_at desc");
        if (null != financialPartakeDTO.getId() && !financialPartakeDTO.getId().equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id < ", financialPartakeDTO.getId());
        }
        ConditionUtil.andCondition(criteria, "status = ", financialPartakeDTO.getFinancialType());
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        List<AppUserFinancialPartake> partakes = appUserFinancialPartakeService.findByCondition(condition);
        return partakes.stream().map(obj -> {
            FinancialUserPartakeVO vo = new FinancialUserPartakeVO();
            AppFinancial financial = findById(obj.getFinancialId());
            vo.setBaseTokenName(financial.getBaseTokenName());
            vo.setName(financial.getName());
            vo.setTokenName(financial.getTokenName());
            vo.setTimes(financial.getTimes().intValue() - obj.getTimes());
            vo.setId(obj.getId());
            if (financialPartakeDTO.getFinancialType() == 1) {
                //计息中的数据需要获取的是昨日收益
                BigDecimal value = appUserFinancialIncomeService.getLastDay(userId, obj.getId());
                vo.setValue(value);
            } else {
                vo.setValue(obj.getIncome());
            }
            vo.setPartake(obj.getValue());
            return vo;
        }).collect(Collectors.toList());
    }

    public List<FinancialSimpleVO> getFinancialList(PageDTO pageDTO, BigInteger id) {
        Condition condition = new Condition(AppFinancial.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "status = ", 1);
        PageHelper.startPage(1, pageDTO.getPageSize(), "id desc");
        if (null != id && !id.equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id < ", id);
        }
        List<AppFinancial> list = findByCondition(condition);
        return list.stream().map(obj -> {
            FinancialSimpleVO vo = new FinancialSimpleVO();
            BeanUtils.copyProperties(obj, vo);
            return vo;
        }).collect(Collectors.toList());
    }

}