package com.mvc.cryptovault.console;

import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.console.service.AppProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * TODO 暂时使用简单定时,后续修改为服务调度
 *
 * @author qiyichen
 * @create 2018/12/7 17:21
 */
//@Component
//@Log4j
public class Job {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    AppProjectService appProjectService;

    /**
     * 修改任务状态
     */
    @Scheduled(cron = "*/60 * * * * ?")
    public void newAccount() {
        final String key = RedisConstant.PROJECT_START;
        Boolean result = getRedisLock(key);
        if (result) {
            return;
        }
//        List<AppProject> list = appProjectService.selectStart();
//        list.stream().forEach(appProject -> {
//            appProject.setStatus(1);
//            appProjectService.update(appProject);
//            appProjectService.updateAllCache("id desc");
//            appProjectService.updateCache(appProject.getId());
//            //设置初始价格
//            appProjectService.setStartPrice();
//        });
    }

    private Boolean getRedisLock(String key) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] keyByte = serializer.serialize(key);
                byte[] valueByte = serializer.serialize(String.valueOf(System.currentTimeMillis()));
                return connection.setNX(keyByte, valueByte);
            }
        });
    }

    /**
     * 发送BTC手续费
     */
    @Scheduled(cron = "*/1 * * * * ?")
    public void senBtcGas() {
    }

    /**
     * 发送解锁收益
     */
    public void sendProject() {
    }

    /**
     * 生成k线数据
     */
    public void kLine() {
    }

}
