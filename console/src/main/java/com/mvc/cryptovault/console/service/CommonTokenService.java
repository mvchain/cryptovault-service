package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.CommonPair;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.CommonTokenControl;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.common.bean.vo.OrderInfoVO;
import com.mvc.cryptovault.common.bean.vo.PairVO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.CommonTokenHistoryMapper;
import com.mvc.cryptovault.console.dao.CommonTokenMapper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommonTokenService extends AbstractService<CommonToken> implements BaseService<CommonToken> {

    @Autowired
    CommonTokenMapper commonTokenMapper;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;
    @Autowired
    CommonPairService commonPairService;
    @Autowired
    CommonTokenHistoryMapper commonTokenHistoryMapper;
    @Autowired
    CommonTokenControlService commonTokenControlService;
    @Autowired
    AppUserBalanceService appUserBalanceService;

    public List<PairVO> getPair(Integer pairType) {
        List<PairVO> result = new ArrayList<>();
        CommonPair pair = new CommonPair();
        pair.setBaseTokenId(BigInteger.valueOf(pairType));
        List<CommonPair> list = commonPairService.findByEntity(pair);
        list.forEach(obj -> {
            CommonToken token = findById(obj.getTokenId());
            CommonTokenPrice price = commonTokenPriceService.findById(obj.getTokenId());
            BigDecimal lastValue = get24HBefore(obj.getTokenId());
            PairVO vo = new PairVO();
            vo.setPair(obj.getPairName());
            vo.setTokenImage(token.getTokenImage());
            vo.setTokenName(token.getTokenName());
            vo.setRatio(price.getTokenPrice());
            vo.setTokenId(token.getId());
            vo.setPairId(pair.getId());
            if (null == lastValue) {
                vo.setIncrease(vo.getRatio().floatValue());
            } else {
                Float increase = lastValue.divide(price.getTokenPrice()).setScale(2, RoundingMode.HALF_DOWN).floatValue();
                vo.setIncrease(increase);
            }
            result.add(vo);
        });
        return result;
    }

    public BigDecimal get24HBefore(BigInteger tokenId) {
        String key = "commonTokenHistory".toUpperCase() + "_24H_BEFORE" + tokenId;
        String obj = redisTemplate.boundValueOps(key).get();
        BigDecimal price = null;
        if (StringUtils.isBlank(obj)) {
            price = update24HBeforePrice(tokenId, key);
        }
        return price;
    }

    @Nullable
    public BigDecimal update24HBeforePrice(BigInteger tokenId, String key) {
        BigDecimal price;
        price = commonTokenHistoryMapper.get24HBefore(tokenId, System.currentTimeMillis());
        if (null == price) {
            price = commonTokenHistoryMapper.getFirst(tokenId);
        }
        if (null != price) {
            redisTemplate.boundValueOps(key).set(String.valueOf(price));
        }
        return price;
    }

    public OrderInfoVO getInfo(BigInteger userId, BigInteger pairId, Integer transactionType) {
        OrderInfoVO vo = new OrderInfoVO();
        CommonPair pair = commonPairService.findById(pairId);
        CommonTokenPrice price = commonTokenPriceService.findById(pair.getTokenId());
        CommonTokenControl tokenControl = commonTokenControlService.findById(pair.getTokenId());
        vo.setBalance(appUserBalanceService.getBalanceByTokenId(userId, pair.getBaseTokenId()));
        vo.setTokenBalance(appUserBalanceService.getBalanceByTokenId(userId, pair.getTokenId()));
        vo.setPrice(price.getTokenPrice());
        if (transactionType.equals(BusinessConstant.TRANSACTION_TYPE_BUY)) {
            vo.setMin(tokenControl.getBuyMin());
            vo.setMax(tokenControl.getBuyMax());
        } else {
            vo.setMin(tokenControl.getSellMin());
            vo.setMax(tokenControl.getSellMax());
        }
        return vo;
    }
}