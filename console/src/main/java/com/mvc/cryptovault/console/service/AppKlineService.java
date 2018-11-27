package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.AppKline;
import com.mvc.cryptovault.common.bean.vo.KLineVO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppKlineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class AppKlineService extends AbstractService<AppKline> implements BaseService<AppKline> {

    @Autowired
    AppKlineMapper appKlineMapper;

    public KLineVO getKLine(BigInteger pairId) {
        String key = "AppKline".toUpperCase() + "_" + pairId;
        List<String> list = redisTemplate.boundListOps(key).range(0, 999);
        if (null == list || list.size() == 0) {
            list = resetKline(pairId);
        }
        Long[] x = new Long[list.size()];
        BigDecimal[] y = new BigDecimal[list.size()];
        for (int i = 0; i < list.size(); i++) {
            if("".equals(list.get(i))){
                KLineVO vo = new KLineVO();
                vo.setTimeX(new Long[]{});
                vo.setValueY(new BigDecimal[]{});
                return vo;
            }
            AppKline kline = JSON.parseObject(list.get(i), AppKline.class);
            x[i] = kline.getKlineTime();
            y[i] = kline.getPrice();
        }
        KLineVO vo = new KLineVO();
        vo.setValueY(y);
        vo.setTimeX(x);
        return vo;
    }

    public List<String> resetKline(BigInteger pairId) {
        String key = "AppKline".toUpperCase() + "_" + pairId;
        Long start = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7;
        Long step = 1000 * 60 * 60L;
        redisTemplate.delete(key);
        for (Long i = start; i < System.currentTimeMillis(); i = i + step) {
            AppKline appKline = appKlineMapper.findByTime(pairId, i);
            if (null != appKline) {
                redisTemplate.boundListOps(key).rightPush(JSON.toJSONString(appKline));
            }
        }
        if(redisTemplate.boundListOps(key).size()==0){
            redisTemplate.boundListOps(key).rightPush("");
        }
        return redisTemplate.boundListOps(key).range(0, 999);
    }

}