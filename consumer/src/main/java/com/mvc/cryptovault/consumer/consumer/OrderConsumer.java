package com.mvc.cryptovault.consumer.consumer;

import com.mvc.cryptovault.common.bean.AppOrder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.spring.starter.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.starter.core.RocketMQListener;
import org.apache.rocketmq.spring.starter.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

/**
 * @author qiyichen
 * @create 2018/12/25 14:53
 */
//@Slf4j
//@Service
//@RocketMQMessageListener(topic = "${spring.rocketmq.orderTopic}", consumerGroup = "order-paid-consumer")
public class OrderConsumer implements RocketMQListener<AppOrder>, RocketMQPushConsumerLifecycleListener {


    @Override
    public void onMessage(AppOrder message) {

//        log.info("------- MessageExtConsumer received message, msgId:{}, body:{} ", message.getMsgId(), new String(message.getBody()));
    }

    @Override
    public void prepareStart(DefaultMQPushConsumer consumer) {
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_TIMESTAMP);
        consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis()));
    }
}
