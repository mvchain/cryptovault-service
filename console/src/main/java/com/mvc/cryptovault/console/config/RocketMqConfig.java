package com.mvc.cryptovault.console.config;

import com.mvc.cryptovault.common.util.RocketMqUtil;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.starter.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author qiyichen
 * @create 2018/12/25 16:22
 */
//@Component
public class RocketMqConfig {

    @Resource
    private DefaultMQProducer producer;
    @Autowired
    RocketMQTemplate rocketMQTemplate;


    @Bean
    public RocketMqUtil rocketMqUtil() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        RocketMqUtil.setProducer(producer);
        return new RocketMqUtil();
    }
}
