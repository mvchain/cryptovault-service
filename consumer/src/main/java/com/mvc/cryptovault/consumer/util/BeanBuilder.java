package com.mvc.cryptovault.consumer.util;

import com.mvc.cryptovault.common.bean.AppUserTransaction;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.constant.BusinessConstant;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author qiyichen
 * @create 2018/12/27 14:46
 */
public class BeanBuilder {

    private static StringRedisTemplate redisTemplate;

    public static AppUserTransaction buildAppUserTransaction(TransactionBuyDTO dto) {
        Long time = System.currentTimeMillis();
        AppUserTransaction transaction = new AppUserTransaction();
        transaction.setUpdatedAt(time);
        transaction.setCreatedAt(time);
        transaction.setOrderNumber(getOrderNumber());
        transaction.setPairId(dto.getPairId());
        transaction.setPrice(dto.getPrice());
        transaction.setTransactionType(dto.getTransactionType());
        transaction.setUserId(dto.getUserId());
        transaction.setValue(dto.getValue());
        return transaction;
    }

    public static String getOrderNumber() {
        Long id = redisTemplate.boundValueOps(BusinessConstant.APP_PROJECT_ORDER_NUMBER).increment();
        return "P" + String.format("%09d", id);
    }

    public static StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public static void setRedisTemplate(StringRedisTemplate redisTemplate) {
        BeanBuilder.redisTemplate = redisTemplate;
    }
}

