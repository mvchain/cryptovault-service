package com.mvc.cryptovault.dashboard.controller;

import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.util.BaseContextHandler;
import com.mvc.cryptovault.dashboard.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/6 17:11
 */
public class BaseController {

    @Autowired
    protected StringRedisTemplate redisTemplate;
    protected RestTemplate restTemplate = new RestTemplate();

    @Autowired
    AdminService adminService;
    @Autowired
    BlockService blockService;
    @Autowired
    ProjectService projectService;
    @Autowired
    TokenService tokenService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    UserService userService;

    protected BigInteger getUserId() {
        BigInteger userId = (BigInteger) BaseContextHandler.get("userId");
        return userId;
    }

    protected BigInteger getUserIdBySign(String sign) {
        Assert.isTrue(!StringUtils.isEmpty(sign) && sign.length() > 32, "请登录后下载");
        String str = sign.substring(32);
        BigInteger userId = new BigInteger(str);
        String key = RedisConstant.EXPORT + userId;
        String result = redisTemplate.opsForValue().get(key);
        Assert.isTrue(null != result && result.equalsIgnoreCase(sign), "请登录后下载");
        BaseContextHandler.set("userId", userId);
        redisTemplate.delete(key);
        return userId;
    }

}
