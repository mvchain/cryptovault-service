package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AppFinancial;
import com.mvc.cryptovault.common.bean.AppUserFinancialPartake;
import com.mvc.cryptovault.common.bean.dto.FinancialBuyDTO;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppUserFinancialPartakeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AppUserFinancialPartakeService extends AbstractService<AppUserFinancialPartake> implements BaseService<AppUserFinancialPartake> {

    @Autowired
    AppUserFinancialPartakeMapper appUserFinancialPartakeMapper;
    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    AppOrderService appOrderService;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;
    @Autowired
    CommonTokenService commonTokenService;

    public BigDecimal getPartake(BigInteger userId, BigInteger id) {
        BigDecimal value = appUserFinancialPartakeMapper.getPartake(userId, id);
        return value;

    }

    public BigDecimal getBalance(BigInteger userId) {
        List<AppUserFinancialPartake> partake = appUserFinancialPartakeMapper.getBalance(userId);
        BigDecimal balance = partake.stream().map(obj -> (obj.getValue().multiply(commonTokenPriceService.findById(obj.getBaseTokenId()).getTokenPrice())).add(obj.getIncome().multiply(commonTokenPriceService.findById(obj.getTokenId()).getTokenPrice()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        return balance;
    }

    public BigDecimal getIncome(BigInteger userId) {
        PageHelper.clearPage();
        List<AppUserFinancialPartake> income = appUserFinancialPartakeMapper.getIncome(userId);
        BigDecimal balance = income.stream().map(obj -> obj.getIncome().multiply(commonTokenPriceService.findById(obj.getTokenId()).getTokenPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
        return balance;

    }

    public void buy(AppFinancial appFinancial, FinancialBuyDTO financialBuyDTO, BigInteger userId) {
        Long time = System.currentTimeMillis();
        AppUserFinancialPartake appUserFinancialPartake = new AppUserFinancialPartake();
        appUserFinancialPartake.setStatus(1);
        appUserFinancialPartake.setUpdatedAt(time);
        appUserFinancialPartake.setCreatedAt(time);
        appUserFinancialPartake.setFinancialId(appFinancial.getId());
        appUserFinancialPartake.setIncome(BigDecimal.ZERO);
        appUserFinancialPartake.setOrderNumber(getOrderNumber());
        appUserFinancialPartake.setShadowValue(BigDecimal.ZERO);
        appUserFinancialPartake.setTimes(0);
        appUserFinancialPartake.setTokenId(appFinancial.getTokenId());
        appUserFinancialPartake.setBaseTokenId(appFinancial.getBaseTokenId());
        appUserFinancialPartake.setUserId(userId);
        appUserFinancialPartake.setValue(financialBuyDTO.getValue());
        save(appUserFinancialPartake);
        appUserBalanceService.updateBalance(userId, appFinancial.getBaseTokenId(), BigDecimal.ZERO.subtract(financialBuyDTO.getValue()));
        appOrderService.saveFinancialOrder(appUserFinancialPartake, appFinancial);
    }

    private String getNowDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String str = sdf.format(calendar.getTime());
        return str;
    }

    public BigDecimal getIncomeDay(AppUserFinancialPartake partake, AppFinancial appFinancial) {
        if (appFinancial.getTimes().equals(partake.getTimes())) {
            return BigDecimal.ZERO;
        }
        String key = RedisConstant.APPFINANCIAL_RATIO + getNowDate() + appFinancial.getId();
        Object ratioStr = redisTemplate.opsForValue().get(key);
        if (null == ratioStr) {
            ratioStr = (Math.random() * (appFinancial.getIncomeMax() - appFinancial.getIncomeMin()) + appFinancial.getIncomeMin()) / 365;
            redisTemplate.opsForValue().set(key, String.valueOf(ratioStr), 1, TimeUnit.DAYS);
        }
        Float ratio = Float.valueOf(String.valueOf(ratioStr));
        return partake.getValue().divide(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(ratio)).multiply(BigDecimal.valueOf(appFinancial.getRatio()));
    }


    public static void main(String[] args) {
        Float a = 10f;
        Float b = 15f;
        System.out.println();
    }

    public void addShadow(BigInteger userId, BigDecimal incomeParent, BigInteger id) {
        //校验父级是否参与项目
        AppUserFinancialPartake partake = getFirstPartake(userId, id);
        if (null == partake) {
            return;
        }
        appUserFinancialPartakeMapper.updateShadow(partake.getId(), incomeParent, System.currentTimeMillis(), partake.getUpdatedAt());
        updateCache(partake.getId());
    }

    private AppUserFinancialPartake getFirstPartake(BigInteger userId, BigInteger appFinancialId) {
        Condition condition = new Condition(AppUserFinancialPartake.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "user_id = ", userId);
        ConditionUtil.andCondition(criteria, "financial_id = ", appFinancialId);
        PageHelper.startPage(1, 1);
        List<AppUserFinancialPartake> list = findByCondition(condition);
        return list.size() == 0 ? null : list.get(0);
    }
}