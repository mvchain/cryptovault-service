package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.bean.dto.*;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.FinancialMapper;
import org.apache.commons.lang3.StringUtils;
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
    AppFinancialDetailService appFinancialDetailService;
    @Autowired
    AppOrderService appOrderService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;

    private final static String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    private final static String RULE_ORDER = "^[P]{1}[0-9]{9}$";

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
        BigDecimal addValue = financial.getAddSold() == null || financial.getAddSold().equals(BigDecimal.ZERO) ? BigDecimal.ZERO : financial.getAddSold().divide(BigDecimal.valueOf(100)).multiply(financial.getLimitValue());
        Assert.isTrue(financialBuyDTO.getTransactionPassword().equals(user.getTransactionPassword()), MessageConstants.getMsg("USER_TRANS_PASS_WRONG"));
        Assert.isTrue(financialBuyDTO.getValue().compareTo(financial.getMinValue()) >= 0, MessageConstants.getMsg("APP_TRANSACTION_MIN_OVER"));
        BigDecimal partake = appUserFinancialPartakeService.getPartake(userId, id);
        Assert.isTrue(partake.add(financialBuyDTO.getValue()).compareTo(financial.getUserLimit()) <= 0, MessageConstants.getMsg("PROJECT_LIMIT_OVER"));
        BigDecimal balance = appUserBalanceService.getBalanceByTokenId(userId, financial.getBaseTokenId());
        Integer num = financialMapper.updateSold(financial.getId(), financialBuyDTO.getValue(), addValue);
        Assert.isTrue(num == 1, MessageConstants.getMsg("PROJECT_LIMIT_OVER"));
        Assert.isTrue(balance.compareTo(financialBuyDTO.getValue()) >= 0, MessageConstants.getMsg("INSUFFICIENT_BALANCE"));
        appUserFinancialPartakeService.buy(financial, financialBuyDTO, userId);
        if (addValue.add(financial.getSold()).compareTo(financial.getLimitValue()) >= 0) {
            financial = financialMapper.selectByPrimaryKey(financial.getId());
            financial.setStatus(2);
            update(financial);
        }
        updateCache(financial.getId());
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
        vo.setIncomeMax(appFinancial.getShowIncomeMax());
        vo.setIncomeMin(appFinancial.getShowIncomeMin());
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
        vo.setIncomeMin(appFinancial.getShowIncomeMin());
        vo.setIncomeMax(appFinancial.getShowIncomeMax());
        vo.setLimitValue(appFinancial.getLimitValue());
        BigDecimal sold = appFinancial.getAddSold() == null || appFinancial.getAddSold().equals(BigDecimal.ZERO) ? appFinancial.getSold() : appFinancial.getSold().add(appFinancial.getLimitValue().multiply(appFinancial.getAddSold().divide(BigDecimal.valueOf(100))));
        vo.setSold(sold.compareTo(appFinancial.getLimitValue()) > 0 ? appFinancial.getLimitValue() : sold);
        return vo;
    }

    public BigDecimal getFinancialBalanceSum(BigInteger userId) {
        BigDecimal balance = appUserFinancialPartakeService.getBalance(userId);
        return balance;
    }

    public FinancialBalanceVO getFinancialBalance(BigInteger userId) {
        FinancialBalanceVO vo = new FinancialBalanceVO();
        BigDecimal income = appUserFinancialPartakeService.getIncome(userId);
        BigDecimal lastIncome = appUserFinancialIncomeService.getLast(userId);
        vo.setBalance(getFinancialBalanceSum(userId));
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
                BigDecimal value = appUserFinancialIncomeService.getLastDay(userId, obj.getId(), obj.getTokenId());
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
        ConditionUtil.andCondition(criteria, "status in (0,1)");
        ConditionUtil.andCondition(criteria, "visible = ", 1);
        PageHelper.startPage(1, pageDTO.getPageSize(), "id desc");
        if (null != id && !id.equals(BigInteger.ZERO)) {
            ConditionUtil.andCondition(criteria, "id < ", id);
        }
        List<AppFinancial> list = findByCondition(condition);
        return list.stream().map(obj -> {
            FinancialSimpleVO vo = new FinancialSimpleVO();
            BeanUtils.copyProperties(obj, vo);
            vo.setLimitValue(obj.getLimitValue());
            vo.setIncomeMax(obj.getShowIncomeMax() == null ? obj.getIncomeMax() : obj.getShowIncomeMax());
            vo.setIncomeMin(obj.getShowIncomeMin() == null ? obj.getIncomeMin() : obj.getShowIncomeMin());
            BigDecimal sold = obj.getAddSold() == null || obj.getAddSold().equals(BigDecimal.ZERO) ? obj.getSold() : obj.getSold().add(obj.getLimitValue().multiply(obj.getAddSold().divide(BigDecimal.valueOf(100))));
            vo.setSold(sold.compareTo(obj.getLimitValue()) > 0 ? obj.getLimitValue() : sold);
            return vo;
        }).collect(Collectors.toList());
    }

    public List<AppFinancial> getDFinancialList(PageDTO pageDTO, String financialName) {
        Condition condition = new Condition(AppFinancial.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "name = ", financialName);
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderBy());
        return findByCondition(condition);
    }

    public AppFinancialDetailVO getDFinancialDetail(BigInteger id) {
        AppFinancial financial = findById(id);
        AppFinancialDetailVO vo = new AppFinancialDetailVO();
        if (null == financial) {
            return null;
        }
        BeanUtils.copyProperties(financial, vo);
        vo.setContent(appFinancialContentService.findById(id));
        vo.setDetails(appFinancialDetailService.findDetails(id));
        vo.setNextIncome(Float.valueOf(appUserFinancialPartakeService.getIncomeNextDay(financial)));
        return vo;
    }

    public void saveAppFinancial(AppFinancialDTO appFinancialDTO) {
        Long time = System.currentTimeMillis();
        AppFinancial appFinancial = new AppFinancial();
        BeanUtils.copyProperties(appFinancialDTO, appFinancial);
        appFinancial.setUpdatedAt(time);
        appFinancial.setCreatedAt(time);
        appFinancial.setStatus(0);
        appFinancial.setSold(BigDecimal.ZERO);
        save(appFinancial);
        updateCache(appFinancial.getId());
        appFinancialDTO.getContent().setFinancialId(appFinancial.getId());
        appFinancialContentService.save(appFinancialDTO.getContent());
        appFinancialContentService.updateCache(appFinancial.getId());
        appFinancialDetailService.updateDetail(appFinancial.getId(), appFinancialDTO.getDetails());
        if (null != appFinancialDTO.getNextIncome()) {
            appUserFinancialPartakeService.setIncomeNextDay(appFinancial.getId(), appFinancialDTO.getNextIncome());
        }
    }

    public void updateAppFinancial(AppFinancialDTO appFinancialDTO) {
        AppFinancial appFinancial = new AppFinancial();
        BeanUtils.copyProperties(appFinancialDTO, appFinancial);
        appFinancial.setUpdatedAt(System.currentTimeMillis());
        update(appFinancial);
        updateCache(appFinancialDTO.getId());
        appFinancialDTO.getContent().setFinancialId(appFinancialDTO.getId());
        appFinancialContentService.update(appFinancialDTO.getContent());
        appFinancialContentService.updateCache(appFinancialDTO.getId());
        appFinancialDetailService.updateDetail(appFinancialDTO.getId(), appFinancialDTO.getDetails());
        if (null != appFinancialDTO.getNextIncome()) {
            appUserFinancialPartakeService.setIncomeNextDay(appFinancial.getId(), appFinancialDTO.getNextIncome());
        }
    }

    public PageInfo<AppFinancialOrderVO> getFinancialOrderList(BigInteger id, PageDTO pageDTO, String searchKey, Integer status) {
        Condition condition = new Condition(AppUserFinancialPartake.class);
        Example.Criteria criteria = condition.createCriteria();
        PageHelper.startPage(pageDTO.getPageNum(), pageDTO.getPageSize(), pageDTO.getOrderBy());
        ConditionUtil.andCondition(criteria, "status = ", status);
        ConditionUtil.andCondition(criteria, "financial_id = ", id);
        if (StringUtils.isNotBlank(searchKey)) {
            if (searchKey.matches(RULE_EMAIL)) {
                AppUser user = appUserService.findOneBy("email", searchKey);
                if (null == user) {
                    return null;
                }
                ConditionUtil.andCondition(criteria, "user_id = ", user.getId());
            } else if (searchKey.matches(RULE_ORDER)) {
                ConditionUtil.andCondition(criteria, "order_number = ", searchKey);
            } else {
                AppUser user = appUserService.findOneBy("nickname", searchKey);
                if (null == user) {
                    return null;
                }
                ConditionUtil.andCondition(criteria, "user_id = ", user.getId());
            }
        }
        List<AppUserFinancialPartake> list = appUserFinancialPartakeService.findByCondition(condition);
        PageInfo result = new PageInfo(list);
        List<AppFinancialOrderVO> vos = list.stream().map(obj -> {
            AppFinancialOrderVO vo = new AppFinancialOrderVO();
            AppUser user = appUserService.findById(obj.getUserId());
            AppFinancial financial = findById(obj.getFinancialId());
            CommonTokenPrice basePrice = commonTokenPriceService.findById(obj.getBaseTokenId());
            CommonTokenPrice tokenPrice = commonTokenPriceService.findById(obj.getTokenId());
            BeanUtils.copyProperties(obj, vo);
            vo.setEmail(user.getEmail());
            vo.setFinancialName(financial.getName());
            vo.setTokenName(financial.getTokenName());
            vo.setBaseTokenName(financial.getBaseTokenName());
            vo.setNickname(user.getNickname());
            BigDecimal price = obj.getIncome().multiply(tokenPrice.getTokenPrice()).add(obj.getValue().multiply(basePrice.getTokenPrice()));
            vo.setPrice(price);
            return vo;
        }).collect(Collectors.toList());
        result.setList(vos);
        return result;
    }

    public void updateStatus() {
        Long currentTimeMillis = System.currentTimeMillis();
        Integer result1 = financialMapper.updateProjectStartStatus(currentTimeMillis);
        Integer result2 = financialMapper.updateProjectStopStatus(currentTimeMillis);
        if (result1 > 0 || result2 > 0) {
            updateAllCache("id desc");
        }
    }
}