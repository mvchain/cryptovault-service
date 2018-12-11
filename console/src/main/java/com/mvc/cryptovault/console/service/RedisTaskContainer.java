package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.console.bean.bo.BlockTransactionBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Component
public class RedisTaskContainer {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static int runTaskThreadNum = 2;
    private static ExecutorService es = Executors.newFixedThreadPool(runTaskThreadNum);
    private RedisQueue redisQueue = null;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    BlockTransactionService blockTransactionService;

    @PostConstruct
    private void init() {
        redisQueue = new RedisQueue(redisTemplate);

        Consumer<String> consumer = (data) -> {
            try {
                BlockTransactionBO blockTransactionBO = JSON.parseObject(data, BlockTransactionBO.class);
                blockTransactionService.doSendTransaction(blockTransactionBO.getUserId(), blockTransactionBO.getTransactionDTO());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }

            return;
        };

        //提交线程
        for (int i = 0; i < runTaskThreadNum; i++) {
            es.execute(
                    new OrderSendRedisConsumer(this, consumer)
            );
        }
    }

    public RedisQueue getRedisQueue() {
        return redisQueue;
    }

}