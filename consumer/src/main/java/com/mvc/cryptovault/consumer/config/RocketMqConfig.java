package com.mvc.cryptovault.consumer.config;

import com.mvc.cryptovault.common.util.RocketMqUtil;
import com.mvc.cryptovault.consumer.util.BeanBuilder;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * @author qiyichen
 * @create 2018/12/25 16:22
 */
@Component
public class RocketMqConfig {

    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    DefaultMQProducer producer;
//
//    @Bean
//    public DefaultMQProducer defaultMQProducer() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
//        DefaultMQProducer producer = new
//                DefaultMQProducer("cryptovault-service");
//        // Specify name server addresses.
//        producer.setNamesrvAddr("192.168.205.134:9876");
//        //Launch the instance.
//        producer.start();
//        Message msg = new Message("TopicTest" /* Topic */,
//                "TagA" /* Tag */,
//                ("Hello RocketMQ " +
//                        1).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
//        );
//        producer.send(msg);
//        return producer;
//    }

    @Bean
    public RocketMqUtil rocketMqUtil() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
//        producer.send(new Message("TopicTest", "TagA", "111".getBytes()));
        RocketMqUtil.setProducer(producer);
        BeanBuilder.setRedisTemplate(redisTemplate);
        return new RocketMqUtil();
    }
}
