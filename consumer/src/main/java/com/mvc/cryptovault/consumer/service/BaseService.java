package com.mvc.cryptovault.consumer.service;

import com.mvc.cryptovault.common.bean.AppUserTransaction;
import com.mvc.cryptovault.consumer.common.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author qiyichen
 * @create 2018/12/27 15:03
 */
public abstract class BaseService<T> {

    @Autowired
    protected MyMapper<T> mapper;

    public void save(T t){
        mapper.insert(t);
    };
}
