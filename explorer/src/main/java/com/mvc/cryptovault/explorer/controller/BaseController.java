package com.mvc.cryptovault.explorer.controller;

import com.mvc.cryptovault.explorer.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author qiyichen
 * @create 2018/11/6 17:11
 */
public class BaseController {

    @Autowired
    protected StringRedisTemplate redisTemplate;

    @Autowired
    BlockService blockService;

}
