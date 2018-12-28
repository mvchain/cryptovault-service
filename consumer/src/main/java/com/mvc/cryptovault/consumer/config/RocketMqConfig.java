package com.mvc.cryptovault.consumer.config;

import com.mvc.cryptovault.common.util.RocketMqUtil;
import com.mvc.cryptovault.consumer.util.BeanBuilder;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiyichen
 * @create 2018/12/25 16:22
 */
@Component
public class RocketMqConfig {

    @Resource
    private DefaultMQProducer producer;
    @Autowired
    StringRedisTemplate redisTemplate;

    @Bean
    public RocketMqUtil rocketMqUtil() {
        RocketMqUtil.setProducer(producer);
        BeanBuilder.setRedisTemplate(redisTemplate);
        return new RocketMqUtil();
    }
}
