package com.mvc.cryptovault.common.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

/**
 * @author qiyichen
 * @create 2018/12/25 15:04
 */
@Slf4j
public class RocketMqUtil {

    private static DefaultMQProducer producer;

    public static String addToMq(String topic, String tag, Object entity) {
        if (null == entity) {
            return null;
        }
        try {
            byte[] bytes = JSON.toJSONBytes(entity);
            Message msg = new Message(topic, tag, bytes);
            SendResult result = producer.send(msg);
            if (result.getSendStatus().equals(SendStatus.SEND_OK)) {
                writeSuccess(topic, tag, entity, result.getMsgId());
                return result.getMsgId();
            }
            writeError(topic, tag, entity);
            return null;
        } catch (Exception e) {
            writeError(topic, tag, entity);
            return null;
        }
    }

    private static void writeError(String topic, String tag, Object entity) {
        String str = JSON.toJSONString(entity);
        log.error("MqWriteError[topic:{}, tag:{}, entity:{}, time:{}]", topic, tag, str, System.currentTimeMillis());
    }

    private static void writeSuccess(String topic, String tag, Object entity, String msgId) {
        String str = JSON.toJSONString(entity);
        log.error("MqWriteSuccess[topic:{}, tag:{}, entity:{}, time:{}, msgId:{}]", topic, tag, str, System.currentTimeMillis(), msgId);
    }

    public static void setProducer(DefaultMQProducer defaultMQProducer) {
        producer = defaultMQProducer;
    }

}
