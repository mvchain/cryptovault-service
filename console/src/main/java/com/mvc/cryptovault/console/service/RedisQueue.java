package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.console.constant.BusinessConstant;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RedisQueue {

    private BoundListOperations<String, String> listOperations;

    private static Lock lock = new ReentrantLock();

    private RedisTemplate redisTemplate;

    public RedisQueue(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        listOperations = redisTemplate.boundListOps(BusinessConstant.REDIS_QUEUE);
    }

    /**
     * blocking 一直阻塞直到队列里边有数据
     * remove and get last item from queue:BRPOP
     *
     * @return
     */
    public String takeFromTail(int timeout) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            //阻塞获取队列内容
            String result = (String) redisTemplate.opsForList().rightPop(BusinessConstant.REDIS_QUEUE, timeout, TimeUnit.MILLISECONDS);
            return result;
        } finally {
            lock.unlock();
        }
    }

    public String takeFromTail() throws InterruptedException {
        return takeFromTail(1000);
    }

    /**
     * 从队列的头，插入
     */
    public void pushFromHead(String value) {
        listOperations.leftPush(value);
    }

    public void pushFromTail(String value) {
        listOperations.rightPush(value);
    }

    /**
     * noblocking
     *
     * @return null if no item in queue
     */
    public String removeFromHead() {
        return listOperations.leftPop();
    }

    public String removeFromTail() {
        return listOperations.rightPop();
    }

}