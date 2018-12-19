package com.mvc.cryptovault.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.app.feign.ConsoleRemoteService;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.common.constant.RedisConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    @Autowired
    ConsoleRemoteService tokenRemoteService;
    @Autowired
    StringRedisTemplate redisTemplate;
    RestTemplate restTemplate = new RestTemplate();

    final BigInteger BASE_TOKEN_ID_BALANCE = BigInteger.valueOf(2);

    public List<TokenDetailVO> getTokens(BigInteger timestamp) {
        Result<PageInfo<CommonToken>> listData = tokenRemoteService.all(1, 0, 999, timestamp);
        ArrayList<TokenDetailVO> result = new ArrayList<>(listData.getData().getList().size());
        for (CommonToken token : listData.getData().getList()) {
            TokenDetailVO vo = new TokenDetailVO();
            Integer tokenType = 0;
            if (token.getId().equals(BASE_TOKEN_ID_BALANCE)) {
                tokenType = 0;
            } else if (StringUtils.isBlank(token.getTokenType())) {
                tokenType = 1;
            } else {
                tokenType = 2;
            }
            vo.setVisible(token.getVisible());
            vo.setTokenImage(token.getTokenImage());
            vo.setTokenCnName(token.getTokenCnName());
            vo.setTokenEnName(token.getTokenEnName());
            vo.setTokenId(token.getId());
            vo.setTokenType(tokenType);
            vo.setTokenName(token.getTokenName());
            vo.setTimestamp(token.getUpdatedAt());
            result.add(vo);
        }
        return result;
    }


    public List<TokenRatioVO> getBase() {
        Result<PageInfo<CommonTokenPrice>> listData = tokenRemoteService.price();
        List<TokenRatioVO> result = new ArrayList<>(listData.getData().getList().size());
        for (CommonTokenPrice token : listData.getData().getList()) {
            TokenRatioVO vo = new TokenRatioVO();
            vo.setTokenName(token.getTokenName());
            vo.setTokenId(token.getTokenId());
            vo.setRatio(token.getTokenPrice());
            result.add(vo);
        }
        return result;

    }

    public List<ExchangeRateVO> getExchangeRate() {
        List<ExchangeRateVO> rate = new ArrayList<>(3);
        String key = RedisConstant.EXCHANGE_RATE;
        var result = redisTemplate.opsForValue().get(key);
        if (null == result) {
            String url = "http://web.juhe.cn:8080/finance/exchange/rmbquot?key=eca6dd52126d970462ed32a77f72a636&type=1";
            String responseStr = restTemplate.getForObject(url, String.class);
            ExchangeResponse response = JSON.parseObject(responseStr, new TypeReference<ExchangeResponse>() {
            });
            if (response.getResultcode() == 200) {
                rate.add(getRate(response.getResult(), "CNY"));
                rate.add(getRate(response.getResult(), "USD"));
                rate.add(getRate(response.getResult(), "EUR"));
            } else {
                return null;
            }
            result = JSON.toJSONString(rate);
            redisTemplate.opsForValue().set(key, result, 12, TimeUnit.HOURS);
        }
        return JSON.parseArray(result, ExchangeRateVO.class);
    }

    public ExchangeRateVO getRate(Map<String, JSONObject> map, String name) {
        ExchangeRateVO vo = new ExchangeRateVO();
        if ("cny".equalsIgnoreCase(name)) {
            vo.setValue(1f);
            vo.setName("¥ " + name.toUpperCase());
        } else if ("USD".equalsIgnoreCase(name)) {
            Float value = Float.valueOf(map.get("美元").get("bankConversionPri").toString());
            vo.setValue(value / 100);
            vo.setName("$ " + name.toUpperCase());
        } else if ("EUR".equalsIgnoreCase(name)) {
            Float value = Float.valueOf(map.get("欧元").get("bankConversionPri").toString());
            vo.setValue(value / 100);
            vo.setName(name.toUpperCase());
            vo.setName("€ " + name.toUpperCase());
        }
        return vo;
    }

}
