package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.CommonTokenControl;
import com.mvc.cryptovault.common.bean.CommonTokenHistory;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.CommonTokenHistoryMapper;
import com.mvc.cryptovault.console.dao.CommonTokenPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class CommonTokenPriceService extends AbstractService<CommonTokenPrice> implements BaseService<CommonTokenPrice> {

    @Autowired
    CommonTokenPriceMapper commonTokenPriceMapper;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    CommonTokenControlNextService commonTokenControlNextService;
    @Autowired
    CommonTokenHistoryMapper historyMapper;

    public void init(CommonTokenControl tokenControl) {
        //基础币种不支持K线生成
        if (tokenControl.getTokenId().compareTo(BusinessConstant.BASE_TOKEN_ID_USDT) > 0) {
            CommonTokenPrice commonTokenPrice = findById(tokenControl.getTokenId());
            if (null != commonTokenPrice) {
                return;
            }
            CommonToken token = commonTokenService.findById(tokenControl.getTokenId());
            commonTokenPrice = new CommonTokenPrice();
            commonTokenPrice.setTokenId(tokenControl.getTokenId());
            commonTokenPrice.setTokenName(token.getTokenName());
            commonTokenPrice.setTokenPrice(tokenControl.getStartPrice());
            save(commonTokenPrice);
            updateAllCache();
            updateCache(commonTokenPrice.getTokenId());
            //生成下一个价格
            commonTokenControlNextService.next(tokenControl);
        }
    }

    public void updatePrice(BigInteger tokenId, BigDecimal nextPrice) {
        Long time = System.currentTimeMillis();
        CommonTokenPrice price = findById(tokenId);
        price.setTokenPrice(nextPrice);
        update(price);
        CommonTokenHistory commonTokenHistory = new CommonTokenHistory();
        commonTokenHistory.setCreatedAt(time);
        commonTokenHistory.setTokenId(tokenId);
        commonTokenHistory.setPrice(nextPrice);
        historyMapper.insert(commonTokenHistory);
        updateAllCache();
        updateCache(tokenId);
    }
}