package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.CommonTokenControl;
import com.mvc.cryptovault.common.bean.CommonTokenControlNext;
import com.mvc.cryptovault.common.bean.CommonTokenHistory;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.CommonTokenControlNextMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Service
public class CommonTokenControlNextService extends AbstractService<CommonTokenControlNext> implements BaseService<CommonTokenControlNext> {

    @Autowired
    CommonTokenControlNextMapper commonTokenControlNextMapper;
    @Autowired
    CommonTokenControlService commonTokenControlService;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;
    @Autowired
    AppKlineService appKlineService;

    public void next(CommonTokenControl tokenControl) {
        CommonTokenControlNext next = new CommonTokenControlNext();
        next.setTokenId(tokenControl.getTokenId());
        next.setTotalSuccess(BigDecimal.ZERO);
        BigDecimal floatPrice = getNext(tokenControl.getStartPrice(), tokenControl.getWaveMin(), tokenControl.getWaveMax());
        if (null != tokenControl.getNextPrice() && tokenControl.getNextPrice().compareTo(BigDecimal.ZERO) != 0 && tokenControl.getNextPrice().compareTo(tokenControl.getStartPrice()) != 0) {
            //如果设置了初始价格,初始价格小于目标价格为涨
            Integer nextType = tokenControl.getStartPrice().compareTo(tokenControl.getNextPrice()) < 0 ? 1 : 2;
            next.setNextPrice(tokenControl.getNextPrice());
            next.setNextType(nextType);
            next.setFloatPrice(floatPrice);
        } else {
            //默认第一次为涨
            BigDecimal price = getNext(tokenControl.getStartPrice(), tokenControl.getIncreaseMin(), tokenControl.getIncreaseMax());
            next.setNextType(1);
            next.setNextPrice(price);
            next.setFloatPrice(floatPrice);
        }
        save(next);
        updateAllCache();
        updateCache(next.getTokenId());
    }

    /**
     * @param base 基准价格
     * @param min  最小波动比例,单位为%,跌则为负数
     * @param max  最大波动比例,单位为%,跌则为负数
     * @return
     */
    private BigDecimal getNext(BigDecimal base, Float min, Float max) {
        Float minFloat = base.multiply(new BigDecimal(1 + (min / 100))).floatValue();
        Float maxFloat = base.multiply(new BigDecimal(1 + (max / 100))).floatValue();
        BigDecimal value = new BigDecimal(Math.random() * (maxFloat - minFloat) + minFloat);
        return value.setScale(10, RoundingMode.HALF_UP);
    }

    public void updateNext(BigInteger tokenId, CommonTokenControl control) {
        CommonTokenControlNext next = findById(tokenId);
        if (null == next && null == control) {
            return;
        }
        //是否需要改变方向
        Boolean needChange = getNeedChange(control, next);
        BigDecimal base = next.getFloatPrice();
        if (needChange) {
            Float min = next.getNextType() == 1 ? 0f - control.getDecreaseMin() : control.getIncreaseMin();
            Float max = next.getNextType() == 1 ? 0f - control.getDecreaseMax() : control.getIncreaseMax();
            Integer nextType = next.getNextType() == 1 ? 2 : 1;
            BigDecimal value = getNext(base, min, max);
            next.setFloatPrice(next.getNextPrice());
            next.setNextType(nextType);
            next.setNextPrice(value);
            //方向发生变动后清空币种设置中的目标价格
            control.setNextPrice(BigDecimal.ZERO);
            commonTokenControlService.update(control);
        } else {
            Float min = next.getNextType() == 1 ? control.getWaveMin() : 0f - control.getWaveMin();
            Float max = next.getNextType() == 1 ? control.getWaveMax() : 0f - control.getWaveMax();
            BigDecimal value = getNext(base, min, max);
            next.setFloatPrice(value);
        }
        next.setTotalSuccess(BigDecimal.ZERO);
        update(next);
        updateCache(tokenId);
    }

    private Boolean getNeedChange(CommonTokenControl control, CommonTokenControlNext next) {
        BigDecimal nextPrice = null == control.getNextPrice() || BigDecimal.ZERO.compareTo(control.getNextPrice()) == 0 ? next.getNextPrice() : control.getNextPrice();
        //如果此次浮动可能超过目标价格,则此次浮动直接变至目标价格
        Boolean needChange = false;
        Float ratio = next.getNextType() == 1 ? control.getWaveMax() / 100 : 0f - control.getWaveMax() / 100;
        BigDecimal limit = next.getFloatPrice().multiply(BigDecimal.ONE.add(new BigDecimal(ratio)));
        if (next.getNextType() == 1) {
            needChange = limit.compareTo(nextPrice) >= 0;
        } else {
            needChange = limit.compareTo(nextPrice) <= 0;
        }
        return needChange;
    }

    /**
     * 更新总量以及价格
     *
     * @param tokenId
     * @param value
     */
    public void updateTotal(BigInteger tokenId, BigDecimal value) {
        if (value.equals(BigDecimal.ZERO)) {
            return;
        }
        CommonTokenControl control = commonTokenControlService.findById(tokenId);
        CommonTokenControlNext next = findById(tokenId);
        if (null == next) {
            next(control);
            next = findById(tokenId);
        }
        if (next.getTotalSuccess().add(value).compareTo(control.getPriceBase()) < 0) {
            next.setTotalSuccess(next.getTotalSuccess().add(value));
            update(next);
            updateCache(tokenId);
        } else {
            //此次数量-（波动基数-当前数量）的部分将被记入下一次计算
            BigDecimal nowValue = value.subtract(control.getPriceBase().subtract(next.getTotalSuccess()));
            //记录价格变动历史并更新当前价格
            appKlineService.saveHistory(tokenId,  next.getFloatPrice());
            updateNext(tokenId, control);
            updateTotal(tokenId, nowValue);
        }
    }

}