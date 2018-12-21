package com.mvc.cryptovault.console.job;

import com.mvc.cryptovault.common.bean.TokenVolume;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.console.service.CommonTokenControlNextService;
import com.mvc.cryptovault.console.service.CommonTokenControlService;
import com.mvc.cryptovault.console.service.CommonTokenPriceService;
import com.mvc.cryptovault.console.service.TokenVolumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author qiyichen
 * @create 2018/12/10 16:55
 */
@Component
public class PriceRunner implements CommandLineRunner {

    @Autowired
    TokenVolumeService tokenVolumeService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    CommonTokenPriceService commonTokenPriceService;
    @Autowired
    CommonTokenControlService commonTokenControlService;
    @Autowired
    CommonTokenControlNextService commonTokenControlNextService;

    @Override
    @Async
    public void run(String... args) throws Exception {
        try {
            while (true) {
                tokenVolume();
            }
        } catch (Exception e) {
            e.printStackTrace();
            tokenVolume();
        }
    }

    public void tokenVolume() {
        sleep();
        Boolean result = getRedisLock(RedisConstant.TOKEN_VOLUME);
        if (!result) {
            return;
        }
        TokenVolume tokenVolume = getNext();
        if (null != tokenVolume) {
            commonTokenControlNextService.updateTotal(tokenVolume.getTokenId(), tokenVolume.getValue());
            tokenVolume.setUsed(1);
            tokenVolumeService.update(tokenVolume);
        }
        redisTemplate.delete(RedisConstant.TOKEN_VOLUME);
    }

    private void sleep() {
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private TokenVolume getNext() {
        TokenVolume tokenVolume = tokenVolumeService.getNext();
        return tokenVolume;
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
