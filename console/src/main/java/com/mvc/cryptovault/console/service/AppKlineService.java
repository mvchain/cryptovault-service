package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.AppKline;
import com.mvc.cryptovault.common.bean.CommonPair;
import com.mvc.cryptovault.common.bean.CommonTokenHistory;
import com.mvc.cryptovault.common.bean.vo.KLineVO;
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
import java.util.List;

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

    private final String KLINE_HEADER = "KLINE_HEADER_";


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
        String key = KLINE_HEADER + pair.getTokenId();
        String time = (String) redisTemplate.opsForHash().get(key, "TIME");
        if (StringUtils.isBlank(time)) {
            updateKline(pair.getTokenId());
        }
        String xStr = (String) redisTemplate.opsForHash().get(KLINE_HEADER + pair.getTokenId(), "X");
        String yStr = (String) redisTemplate.opsForHash().get(KLINE_HEADER + pair.getTokenId(), "Y");
        KLineVO vo = new KLineVO();
        vo.setTimeX(strArr2ObjArr(xStr, Long.class));
        vo.setValueY(strArr2ObjArr(yStr, BigDecimal.class));
        return vo;

    }

    public void saveHistory(BigInteger tokenId, BigDecimal usdtPrice) {
        CommonTokenHistory history = new CommonTokenHistory();
        history.setCreatedAt(System.currentTimeMillis());
        history.setPrice(usdtPrice);
        history.setTokenId(tokenId);
        commonTokenHistoryMapper.insert(history);
    }
}