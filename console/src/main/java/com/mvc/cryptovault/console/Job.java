package com.mvc.cryptovault.console;

import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.console.service.AppProjectPartakeService;
import com.mvc.cryptovault.console.service.AppProjectService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

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

    }

    /**
     * 生成k线数据
     */
    public void kLine() {
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
