package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.AppKline;
import com.mvc.cryptovault.common.bean.CommonPair;
import com.mvc.cryptovault.common.bean.CommonTokenHistory;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.common.bean.vo.KLineVO;
import com.mvc.cryptovault.common.bean.vo.TickerVO;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.CommonTokenHistoryMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AppKlineService extends AbstractService<AppKline> implements BaseService<AppKline> {

    @Value("${kline.step}")
    private Long step;
    @Value("${kline.start}")
    private Long start;
    @Autowired
    CommonPairService commonPairService;
    @Autowired
    CommonTokenHistoryMapper commonTokenHistoryMapper;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;

    private final String KLINE_HEADER = "KLINE_HEADER_";
    private final Long KLINE_CACHE_TIME = 1000 * 60 * 5L;

    /**
     * @param tokenId
     */
    public void updateKline(BigInteger tokenId) {
        //计算一共需要多少个采集点
        Long point = start / step;
        String key = "AppKline".toUpperCase() + "_" + tokenId;
        Long length = redisTemplate.boundListOps(key).size();
        if (null == length || !length.equals(point)) {
            //需要重新绘制
            resetTokenKline(tokenId);
        } else {
            //如果存在k线，直接取当前最近一次的价格
            CommonTokenHistory appKline = commonTokenHistoryMapper.findByLast(tokenId);
            redisTemplate.boundListOps(key).leftPop();
            if (null == appKline) {
                appKline = new CommonTokenHistory();
                appKline.setCreatedAt(System.currentTimeMillis() - 100);
                appKline.setPrice(BigDecimal.ZERO);
            }
            redisTemplate.boundListOps(key).rightPush(JSON.toJSONString(appKline));
            redisTemplate.opsForHash().put(KLINE_HEADER + tokenId, "TIME", String.valueOf(System.currentTimeMillis() - 100));
        }
    }

    private List<String> resetTokenKline(BigInteger tokenId) {
        String key = "AppKline".toUpperCase() + "_" + tokenId;
        Long now = System.currentTimeMillis();
        Long startTime = now - start;
        redisTemplate.delete(key);
        for (Long i = startTime; i < now; i = i + step) {
            CommonTokenHistory appKline = commonTokenHistoryMapper.findByTime(tokenId, i - 100);
            if (null != appKline) {
                appKline.setCreatedAt(i - 100);
                redisTemplate.boundListOps(key).rightPush(JSON.toJSONString(appKline));
            } else {
                appKline = new CommonTokenHistory();
                appKline.setCreatedAt(i - 100);
                appKline.setPrice(BigDecimal.ZERO);
                redisTemplate.boundListOps(key).rightPush(JSON.toJSONString(appKline));
            }
        }
        Long size = redisTemplate.boundListOps(key).size();
        if (size == 0) {
            redisTemplate.boundListOps(key).rightPush("");
        }
        return redisTemplate.boundListOps(key).range(0, size);
    }

    private <T> T[] strArr2ObjArr(String str, Class<T> clazz) {
        String strArr[] = str.split(",");
        if (clazz.equals(Long.class)) {
            Long arr[] = new Long[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                arr[i] = NumberUtils.parseNumber(strArr[i], Long.class);
            }
            return (T[]) arr;
        } else {
            BigDecimal arr[] = new BigDecimal[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                arr[i] = NumberUtils.parseNumber(strArr[i], BigDecimal.class);
            }
            return (T[]) arr;
        }
    }

    public KLineVO getKLine(BigInteger pairId) {
        CommonPair pair = commonPairService.findById(pairId);
        String key = "AppKline".toUpperCase() + "_" + pair.getTokenId();
        List<String> strList = redisTemplate.boundListOps(key).range(0, redisTemplate.boundListOps(key).size());
        if (strList.size() == 0) {
            resetTokenKline(pair.getTokenId());
        }
        String last = (String) redisTemplate.opsForHash().get(KLINE_HEADER + pair.getTokenId(), "TIME");
        Long time = StringUtils.isBlank(last) ? System.currentTimeMillis() + KLINE_CACHE_TIME : Long.valueOf(last) + KLINE_CACHE_TIME;
        String x = (String) redisTemplate.opsForHash().get(KLINE_HEADER + pair.getTokenId(), "X");
        String y = (String) redisTemplate.opsForHash().get(KLINE_HEADER + pair.getTokenId(), "Y");
        KLineVO vo = new KLineVO();
//        if (time < System.currentTimeMillis() || StringUtils.isBlank(x) || "[]".equals(x)) {
        if (true) {
            //数据已过期,需要重新获取
            Long[] timeX = new Long[strList.size()];
            BigDecimal[] valueY = new BigDecimal[strList.size()];
            for (int i = 0; i < strList.size(); i++) {
                CommonTokenHistory history = JSON.parseObject(strList.get(i), CommonTokenHistory.class);
                timeX[i] = history.getCreatedAt();
                valueY[i] = history.getPrice();
            }
            vo.setTimeX(timeX);
            vo.setValueY(valueY);
            redisTemplate.opsForHash().put(KLINE_HEADER + pair.getTokenId(), "X", JSON.toJSONString(timeX));
            redisTemplate.opsForHash().put(KLINE_HEADER + pair.getTokenId(), "Y", JSON.toJSONString(valueY));
        } else {
            List<Long> xList = JSON.parseArray(x, Long.class);
            List<BigDecimal> yList = JSON.parseArray(y, BigDecimal.class);
            vo.setTimeX(xList.toArray(new Long[]{}));
            vo.setValueY(yList.toArray(new BigDecimal[]{}));
        }
        return vo;

    }

    public void saveHistory(BigInteger tokenId, BigDecimal usdtPrice) {
        if (usdtPrice.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        CommonTokenHistory history = new CommonTokenHistory();
        history.setCreatedAt(System.currentTimeMillis());
        history.setPrice(usdtPrice);
        history.setTokenId(tokenId);
        commonTokenHistoryMapper.insert(history);
        commonTokenPriceService.updatePrice(tokenId, usdtPrice);
    }

    public TickerVO getTickers(BigInteger pairId) {
        CommonPair pair = commonPairService.findById(pairId);
        if (null == pair) {
            return new TickerVO();
        }
        //mvc
        CommonTokenPrice baseToken = commonTokenPriceService.findById(pair.getBaseTokenId());
        String key = "AppKline".toUpperCase() + "_Tickers_" + pair.getTokenId();
        String priceObj = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(priceObj)) {
            TickerVO vo = commonTokenHistoryMapper.findTicker(pair.getTokenId(), System.currentTimeMillis() - RedisConstant.ONE_DAY);
            CommonTokenPrice price = commonTokenPriceService.findOneBy("tokenId", pair.getTokenId());
            if (null == price) {
                return null;
            }
            vo.setPrice(null == price ? BigDecimal.ZERO : baseToken.getTokenPrice().divide(price.getTokenPrice(), 20, RoundingMode.DOWN));
            vo.setLow(BigDecimal.ZERO.compareTo(vo.getLow()) == 0 ? vo.getPrice() : baseToken.getTokenPrice().divide(vo.getLow(), 20, RoundingMode.DOWN));
            vo.setHigh(BigDecimal.ZERO.compareTo(vo.getHigh()) == 0 ? vo.getPrice() : baseToken.getTokenPrice().divide(vo.getHigh(), 20, RoundingMode.DOWN));
            redisTemplate.opsForValue().set(key, JSON.toJSONString(vo), 10, TimeUnit.SECONDS);
            return vo;
        }
        return JSON.parseObject(priceObj, TickerVO.class);
    }

}