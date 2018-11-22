package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.AppUserBalance;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.common.bean.vo.ProjectBuyVO;
import com.mvc.cryptovault.common.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.AppUserBalanceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service
public class AppUserBalanceService extends AbstractService<AppUserBalance> implements BaseService<AppUserBalance> {

    @Autowired
    AppUserBalanceMapper appUserBalanceMapper;
    @Autowired
    AppProjectUserTransactionService appProjectUserTransactionService;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;

    private Comparator comparator = new Comparator<TokenBalanceVO>() {
        @Override
        public int compare(TokenBalanceVO o1, TokenBalanceVO o2) {
            return o1.getTokenId().compareTo(o2.getTokenId());
        }
    };

    public ProjectBuyVO getBalance(BigInteger userId, AppProject appProject) {
        ProjectBuyVO vo = new ProjectBuyVO();
        vo.setBalance(getBalanceByTokenId(userId, appProject.getBaseTokenId()));
        BigDecimal userBuyTotal = appProjectUserTransactionService.getUserBuyTotal(userId, appProject.getId());
        BigDecimal limit = appProject.getProjectLimit().subtract(userBuyTotal);
        vo.setLimitValue(limit);
        return vo;
    }

    public BigDecimal getBalanceByTokenId(BigInteger userId, BigInteger tokenId) {
        String key = "AppUserBalance".toUpperCase() + "_" + userId;
        if (redisTemplate.hasKey(key)) {
            String balance = (String) redisTemplate.boundHashOps(key).get(String.valueOf(tokenId));
            if (StringUtils.isNotBlank(balance)) {
                return NumberUtils.parseNumber(balance, BigDecimal.class);
            }
        }
        BigDecimal result = null;
        AppUserBalance appUserBalance = new AppUserBalance();
        appUserBalance.setTokenId(tokenId);
        appUserBalance.setUserId(userId);
        AppUserBalance userBalance = appUserBalanceMapper.selectOne(appUserBalance);
        if (null == userBalance) {
            result = BigDecimal.ZERO;
        } else {
            result = userBalance.getBalance();
        }
        redisTemplate.boundHashOps(key).put(String.valueOf(tokenId), String.valueOf(result));
        return result;
    }

    public void updateBalance(BigInteger userId, BigInteger baseTokenId, BigDecimal value) {
        String key = "AppUserBalance".toUpperCase() + "_" + userId;
        appUserBalanceMapper.updateBalance(userId, baseTokenId, value);
        AppUserBalance appUserBalance = new AppUserBalance();
        appUserBalance.setTokenId(baseTokenId);
        appUserBalance.setUserId(userId);
        AppUserBalance userBalance = appUserBalanceMapper.selectOne(appUserBalance);
        redisTemplate.boundHashOps(key).put(String.valueOf(baseTokenId), String.valueOf(userBalance.getBalance()));
    }

    public List<TokenBalanceVO> getAsset(BigInteger userId) {
        String key = "AppUserBalance".toUpperCase() + "_" + userId;
        if (!redisTemplate.hasKey(key)) {
            List<AppUserBalance> list = findBy("userId", userId);
            if (null == list) {
                redisTemplate.boundHashOps(key).put("1", "0");
            } else {
                list.forEach(obj -> redisTemplate.boundHashOps(key).put(String.valueOf(obj.getTokenId()), String.valueOf(obj.getBalance())));
            }
        }
        Map<Object, Object> map = redisTemplate.boundHashOps(key).entries();
        List<TokenBalanceVO> result = new ArrayList<>(map.size());
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            TokenBalanceVO vo = new TokenBalanceVO();
            vo.setTokenId(NumberUtils.parseNumber(String.valueOf(entry.getKey()), BigInteger.class));
            vo.setValue(NumberUtils.parseNumber(String.valueOf(entry.getValue()), BigDecimal.class));
            CommonTokenPrice tokenPrice = commonTokenPriceService.findById(vo.getTokenId());
            vo.setRatio(tokenPrice.getTokenPrice());
            vo.setTokenName(tokenPrice.getTokenName());
            result.add(vo);
        }
        Collections.sort(result, comparator);
        return result;
    }

    public void debit(BigInteger userId, BigDecimal value) {
        String key = "AppUserBalance".toUpperCase() + "_" + userId;
        appUserBalanceMapper.updateBalance(userId, BusinessConstant.BASE_TOKEN_ID_BALANCE, value);
        AppUserBalance balance = new AppUserBalance();
        balance.setUserId(userId);
        balance.setTokenId(BusinessConstant.BASE_TOKEN_ID_BALANCE);
        balance = findOneByEntity(balance);
        redisTemplate.boundHashOps(key).put(String.valueOf(balance.getTokenId()), String.valueOf(balance.getBalance()));
    }
}