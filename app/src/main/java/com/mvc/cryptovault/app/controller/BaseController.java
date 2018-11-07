package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * @author qiyichen
 * @create 2018/11/6 17:11
 */
public class BaseController {

    @Autowired
    protected RedisTemplate redisTemplate;
    protected RestTemplate restTemplate = new RestTemplate();

    protected Result mockResult = new Result(500, "服务器错误", null);
}
