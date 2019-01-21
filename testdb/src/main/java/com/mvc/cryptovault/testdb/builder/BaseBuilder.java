package com.mvc.cryptovault.testdb.builder;

import com.mvc.cryptovault.common.bean.AppUserFinancialPartake;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/19 17:19
 */
public abstract class BaseBuilder<T> {
    volatile Long id = 1L;

    public  abstract String getInstance(Integer id);

    public abstract String getHeader();

    public abstract String tableName();

    public String getOrderNumber() {
        ++id;
        return "P" + String.format("%09d", id);
    }

    public Integer getUserId() {
        return (int) (Math.random() * AppUserBuilder.NUMBER) + 1;
    }

    public Integer getPartakeId() {
        return (int) (Math.random() * AppUserFinancialPartakeBuilder.NUMBER) + 1;
    }
}
