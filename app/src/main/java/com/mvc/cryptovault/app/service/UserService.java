package com.mvc.cryptovault.app.service;

import com.mvc.cryptovault.common.bean.dto.UserDTO;
import com.mvc.cryptovault.common.bean.vo.TokenVO;
import com.mvc.cryptovault.common.bean.vo.UserSimpleVO;
import com.mvc.cryptovault.app.feign.UserRemoteService;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.util.JwtHelper;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.common.util.TokenErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    UserRemoteService userRemoteService;
    @Autowired
    StringRedisTemplate redisTemplate;

    public UserSimpleVO getUserById(BigInteger userId) {
        UserSimpleVO vo = new UserSimpleVO();
        Result<AppUser> userResult = userRemoteService.getUserById(userId);
        AppUser user = userResult.getData();
        vo.setHeadImage(user.getHeadImage());
        vo.setNickname(user.getNickname());
        String username = user.getCellphone().substring(0, 3) + "****" + user.getCellphone().substring(7);
        vo.setUsername(username);
        return vo;
    }

    public TokenVO login(UserDTO userDTO) {
        String redisKey = "LOGIN_WRONG_TIME_" + userDTO.getUsername();
        TokenVO vo = new TokenVO();
        Result<AppUser> userResult = userRemoteService.getUserByUsername(userDTO.getUsername());
        AppUser user = userResult.getData();
        Assert.notNull(null != user, MessageConstants.getMsg("USER_NOT_EXIST"));
        Boolean passwordCheck = user.getPassword().equals(userDTO.getPassword());
        if (!passwordCheck) {
            //从第一次错误时间开始计时,10分钟内每次错误则错误次数加1
            Long wrongTimes = redisTemplate.opsForHash().increment(redisKey, "TIME", 1);
            if (wrongTimes.equals(1L)) {
                redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);
            }
            //超过5次错误则抛出错误,下一次需要输入验证码
            if (wrongTimes.equals(5L)) {
                redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
                throw new TokenErrorException(MessageConstants.getMsg("USER_PASS_WRONG_MORE"), 401);
            }
        }
        Assert.isTrue(passwordCheck, MessageConstants.getMsg("USER_PASS_WRONG"));
        String token = JwtHelper.createToken(userDTO.getUsername(), user.getId());
        String refreshToken = JwtHelper.createRefresh(userDTO.getUsername(), user.getId());
        //密码正确后清空错误次数
        redisTemplate.delete(redisKey);
        vo.setRefreshToken(refreshToken);
        vo.setToken(token);
        return vo;
    }
}
