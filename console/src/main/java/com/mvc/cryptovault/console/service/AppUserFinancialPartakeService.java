package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AppFinancial;
import com.mvc.cryptovault.common.bean.AppUserFinancialPartake;
import com.mvc.cryptovault.common.bean.dto.FinancialBuyDTO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppUserFinancialPartakeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

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
        List<AppUserFinancialPartake> income =  appUserFinancialPartakeMapper.getIncome(userId);
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
}