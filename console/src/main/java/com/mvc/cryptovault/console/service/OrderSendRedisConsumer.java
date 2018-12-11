package com.mvc.cryptovault.console.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

/**
 * 暂时只用来处理区跨链订单,其他需求后续修改
 */
public class OrderSendRedisConsumer extends Thread {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private RedisTaskContainer container;
    private Consumer<String> consumer;

    public OrderSendRedisConsumer(RedisTaskContainer container, Consumer<String> consumer) {
        this.container = container;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String value = container.getRedisQueue().takeFromTail();
                //逐个执行
                if (value != null) {
                    try {
                        consumer.accept(value);
                    } catch (Exception e) {
                        logger.error("调用失败", e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("轮循线程异常退出", e);
        }
    }
}