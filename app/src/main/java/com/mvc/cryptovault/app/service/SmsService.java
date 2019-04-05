package com.mvc.cryptovault.app.service;

import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author qyc
 */
//@Service
public class SmsService {

    @Autowired
    private YunpianClient yunpianClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${white.account}")
    String[] whiteAccount;
    public static Queue<String> queue = new ConcurrentLinkedQueue<>();

    private final static Long EXPIRE = 5L;

    public Boolean getSmsValiCode(String mobile) {
        String key = RedisConstant.SMS_VALI_PRE + mobile;
        Boolean result = redisTemplate.hasKey(key);
        if (result) {
            String timeStr = (String) redisTemplate.opsForHash().get(key, "TIME");
            Long time = Long.valueOf(timeStr);
            Assert.isTrue(time + 1000 * 60L <= System.currentTimeMillis(), MessageConstants.getMsg("SMS_WAIT"));
        }
        queue.add(mobile);
        return true;
    }

    public Boolean sendSms(String cellphone) {
        String key = RedisConstant.SMS_VALI_PRE + cellphone;
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        //发送短信API
        Map<String, String> param = yunpianClient.newParam(2);
        param.put(YunpianClient.MOBILE, cellphone);
        param.put(YunpianClient.TEXT, String.format("您的验证码是%s", code));
        Result<SmsSingleSend> r = yunpianClient.sms().single_send(param);
        if (r.isSucc() && !r.getMsg().equalsIgnoreCase("验证码类短信1小时内同一手机号发送次数不能超过3次")) {
            redisTemplate.opsForHash().put(key, "CODE", String.valueOf(code));
            redisTemplate.opsForHash().put(key, "TIME", String.valueOf(System.currentTimeMillis()));
            redisTemplate.expire(key, EXPIRE, TimeUnit.MINUTES);
        }
        return r.isSucc();
    }

    public Boolean checkSmsValiCode(String mobile, String code) {
        String key = RedisConstant.SMS_VALI_PRE + mobile;
        String valiCode = "" + redisTemplate.opsForHash().get(key, "CODE");
        if (Arrays.asList(whiteAccount).contains(mobile) && "555666".equalsIgnoreCase(code)) {
            return true;
        }
        if (ObjectUtils.equals(valiCode, code) && !"null".equalsIgnoreCase(valiCode)) {
            return true;
        }
        return false;
    }


}
