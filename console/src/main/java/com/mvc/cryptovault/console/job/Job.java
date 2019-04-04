package com.mvc.cryptovault.console.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.mvc.cryptovault.common.bean.vo.ExchangeRateVO;
import com.mvc.cryptovault.common.bean.vo.ExchangeResponse;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.service.*;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
    AdminWalletService adminWalletService;
    @Autowired
    AppProjectPartakeService appProjectPartakeService;
    @Autowired
    AppKlineService appKlineService;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    UsdtService usdtService;
    @Autowired
    FinancialService financialService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    AppUserFinancialPartakeService appUserFinancialPartakeService;

    RestTemplate restTemplate = new RestTemplate();
    @Value("${coinmarketcap.key}")
    String apiKey;
    /**
     * 修改任务状态
     */
//    @Scheduled(cron = "*/2 * * * * ?")
//    public void newAccount() {
//        final String key = RedisConstant.PROJECT_START;
//        Boolean result = getRedisLock(key, 5);
//        usdtService.getHotWallet();
//        if (!result) {
//            return;
//        }
//        appProjectService.updateProjectStatus();
//        financialService.updateStatus();
//        redisTemplate.delete(key);
//    }


    /**
     * 发送BTC手续费
     */
//    @Scheduled(cron = "${scheduled.usdt.fee}")
//    public void sendBtcGas() {
//        final String key = RedisConstant.USDT_FEE;
//        Boolean result = getRedisLock(key, 120);
//        if (!result) {
//            return;
//        }
//        usdtService.senBtcGas();
//        redisTemplate.delete(key);
//    }

    /**
     * 生成k线数据
     */

//    @Scheduled(cron = "${scheduled.kline}")
//    public void kLine() {
//        List<CommonToken> tokens = commonTokenService.findKlineToken();
//        tokens.forEach(token -> {
//            final String key = RedisConstant.KLINE_SCHEDULED + token.getId();
//            Boolean result = getRedisLock(key, 120);
//            if (!result) {
//                return;
//            }
//            appKlineService.updateKline(token.getId());
//            String key24Hour = "commonTokenHistory".toUpperCase() + "_24H_BEFORE" + token.getId();
//            //每次更新k线后更新24小时之前的价格,用于涨跌幅展示
//            commonTokenService.update24HBeforePrice(token.getId(), key24Hour);
//            redisTemplate.delete(key);
//        });
//    }
    public String makeAPICall(String uri, List<NameValuePair> parameters) {
        CloseableHttpResponse response = null;
        try {
            String response_content = "";
            URIBuilder query = new URIBuilder(uri);
            query.addParameters(parameters);
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(query.build());
            request.setHeader(HttpHeaders.ACCEPT, "application/json");
            request.addHeader("X-CMC_PRO_API_KEY", apiKey);
            response = client.execute(request);
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return response_content;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 生成usdt和eth的价格（从第三方拉取）
     *
     * @return
     */
    @Scheduled(cron = "${scheduled.usdt}")
    private void updateUsdtPrice() {
        List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
        String usdtUrl = "https://data.block.cc/api/v1/price?symbol=USDT";
        String mvcUrl = "https://data.block.cc/api/v1/price?symbol=MVC";
        String btcUrl = "https://data.block.cc/api/v1/price?symbol=BTC";
        String usdtResult = null;
        String btcResult = null;
        String mvcResult = null;
        try {
            usdtResult = makeAPICall(usdtUrl, paratmers);
            btcResult = makeAPICall(btcUrl, paratmers);
            mvcResult = makeAPICall(mvcUrl, paratmers);
        } catch (RestClientException e) {
            log.warn(e.getMessage());
            return;
        }
        BigDecimal usdtPrice = parseValue(usdtResult, "USDT");
        BigDecimal btcPrice = parseValue(btcResult, "BTC");
        BigDecimal mvcPrice = parseValue(mvcResult, "MVC");
        appKlineService.saveHistory(BusinessConstant.BASE_TOKEN_ID_USDT, usdtPrice);
        appKlineService.saveHistory(BusinessConstant.BASE_TOKEN_ID_BTC, btcPrice);
        appKlineService.saveHistory(BusinessConstant.BASE_TOKEN_ID_VRT, mvcPrice);
    }

    /**
     * 发送解锁收益
     */
    @Scheduled(cron = "${scheduled.project.value}")
    public void sendProject() {
        final String key = RedisConstant.PROJECT_GAINS;
        Boolean result = getRedisLock(key, 3600);
        if (!result) {
            return;
        }
        List<BigInteger> ids = appUserFinancialPartakeService.getAllUserId();
        ids.forEach(userId -> {
            appUserService.sign(userId);
        });
        redisTemplate.delete(key);
    }

    /**
     * @param tokenName
     * @return cny value
     */
    private BigDecimal parseValue(String result, String tokenName) {
        try {
            JSONObject object = JSON.parseObject(result, JSONObject.class);
            return object.getJSONObject("data").getJSONObject(tokenName).getJSONObject("quote").getJSONObject("USD").getBigDecimal("price");
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
            usdRate = list.stream().filter(obj -> obj.getName().equalsIgnoreCase("$ USD")).collect(Collectors.toList()).get(0);

        }
        return usdRate;
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

    private Boolean getRedisLock(String key, Integer timeoutSec) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyByte = serializer.serialize(key);
                byte[] valueByte = serializer.serialize(String.valueOf(System.currentTimeMillis()));
                Boolean result = connection.setNX(keyByte, valueByte);
                redisTemplate.expire(key, timeoutSec, TimeUnit.SECONDS);
                return result;
            }
        });
    }

}
