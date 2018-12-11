package com.mvc.cryptovault.console.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.vo.ExchangeRateVO;
import com.mvc.cryptovault.common.bean.vo.ExchangeResponse;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.service.AppKlineService;
import com.mvc.cryptovault.console.service.AppProjectPartakeService;
import com.mvc.cryptovault.console.service.AppProjectService;
import com.mvc.cryptovault.console.service.CommonTokenService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO 暂时使用简单定时,后续修改为服务调度
 *
 * @author qiyichen
 * @create 2018/12/7 17:21
 */
@Component
@Log4j
public class Job {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    AppProjectService appProjectService;
    @Autowired
    AppProjectPartakeService appProjectPartakeService;
    @Autowired
    AppKlineService appKlineService;
    @Autowired
    CommonTokenService commonTokenService;
    RestTemplate restTemplate = new RestTemplate();

    /**
     * 修改任务状态
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void newAccount() {
        final String key = RedisConstant.PROJECT_START;
        Boolean result = getRedisLock(key);
        if (!result) {
            return;
        }
        appProjectService.updateProjectStatus();
        redisTemplate.delete(key);
    }

    /**
     * 发送解锁收益
     */
    @Scheduled(cron = "${scheduled.project.value}")
    public void sendProject() {
        final String key = RedisConstant.PROJECT_GAINS;
        Boolean result = getRedisLock(key);
        if (!result) {
            return;
        }
        appProjectPartakeService.sendProject();
        redisTemplate.delete(key);
    }

    /**
     * 发送BTC手续费
     */
//    @Scheduled(cron = "{scheduled.kline}")
    public void senBtcGas() {


        final String key = RedisConstant.PROJECT_GAINS;
        Boolean result = getRedisLock(key);
        if (!result) {
            return;
        }
        appProjectPartakeService.sendProject();
        redisTemplate.delete(key);
    }

    /**
     * 生成k线数据
     */
    @Scheduled(cron = "${scheduled.kline}")
    public void kLine() {
        List<CommonToken> tokens = commonTokenService.findKlineToken();
        tokens.forEach(token -> {
            final String key = RedisConstant.KLINE_SCHEDULED + token.getId();
            Boolean result = getRedisLock(key);
            if (!result) {
                return;
            }
            appKlineService.updateKline(token.getId());
            String key24Hour = "commonTokenHistory".toUpperCase() + "_24H_BEFORE" + token.getId();
            //每次更新k线后更新24小时之前的价格,用于涨跌幅展示
            commonTokenService.update24HBeforePrice(token.getId(), key24Hour);
            redisTemplate.delete(key);
        });
    }

    /**
     * 生成usdt和eth的价格（从第三方拉取）
     *
     * @return
     */
    @Scheduled(cron = "${scheduled.usdt}")
    private void updateUsdtPrice() {
        String usdtUrl = "http://query.yahooapis.com/v1/public/yql?q=select data.price from json where url=\"https://data.block.cc/api/v1/price?symbol=USDT\"&format=json";
        String ethUrl = "http://query.yahooapis.com/v1/public/yql?q=select data.price from json where url=\"https://data.block.cc/api/v1/price?symbol=ETH\"&format=json";
        JSONObject usdtResult = restTemplate.getForObject(usdtUrl, JSONObject.class);
        JSONObject ethResult = restTemplate.getForObject(ethUrl, JSONObject.class);
        BigDecimal usdtPrice = parseValue(usdtResult);
        BigDecimal ethPrice = parseValue(ethResult);
        appKlineService.saveHistory(BusinessConstant.BASE_TOKEN_ID_USDT, usdtPrice);
        appKlineService.saveHistory(BusinessConstant.BASE_TOKEN_ID_ETH, ethPrice);

    }

    /**
     * @param obj
     * @return cny value
     */
    private BigDecimal parseValue(JSONObject obj) {
        try {
            ExchangeRateVO usdRate = getRate();
            if (null != usdRate) {
                Object data = ((LinkedHashMap) ((LinkedHashMap) ((LinkedHashMap) ((LinkedHashMap) obj.get("query")).get("results")).get("json")).get("data")).get("price");
                BigDecimal number = NumberUtils.parseNumber(String.valueOf(data), BigDecimal.class);
                return number.multiply(BigDecimal.valueOf(usdRate.getValue()));
            }
            return null;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private ExchangeRateVO getRate() {
        ExchangeRateVO usdRate = null;
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
                usdRate = getRate(response.getResult(), "USD");
                rate.add(usdRate);
                rate.add(getRate(response.getResult(), "EUR"));
                result = JSON.toJSONString(rate);
                redisTemplate.opsForValue().set(key, result, 12, TimeUnit.HOURS);
            }
        } else {
            List<ExchangeRateVO> list = JSON.parseArray(result, ExchangeRateVO.class);
            usdRate = list.stream().filter(obj -> obj.getName().equalsIgnoreCase("USD")).collect(Collectors.toList()).get(0);

        }
        return usdRate;
    }

    public ExchangeRateVO getRate(Map<String, JSONObject> map, String name) {
        ExchangeRateVO vo = new ExchangeRateVO();
        vo.setName(name.toUpperCase());
        if ("cny".equalsIgnoreCase(name)) {
            vo.setValue(1f);
        } else if ("USD".equalsIgnoreCase(name)) {
            Float value = Float.valueOf(map.get("美元").get("bankConversionPri").toString());
            vo.setValue(value / 100);
        } else if ("EUR".equalsIgnoreCase(name)) {
            Float value = Float.valueOf(map.get("欧元").get("bankConversionPri").toString());
            vo.setValue(value / 100);
        }
        return vo;
    }

    private Boolean getRedisLock(String key) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyByte = serializer.serialize(key);
                byte[] valueByte = serializer.serialize(String.valueOf(System.currentTimeMillis()));
                Boolean result = connection.setNX(keyByte, valueByte);
                redisTemplate.expire(key, 5, TimeUnit.SECONDS);
                return result;
            }
        });
    }
}
