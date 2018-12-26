package com.mvc.cryptovault.consumer.config;

import com.mvc.cryptovault.common.util.RocketMqUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
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

    @Bean
    public RocketMqUtil rocketMqUtil() {
        RocketMqUtil.setProducer(producer);
        return new RocketMqUtil();
    }
}
