package com.mvc.cryptovault.app.service;

import com.mvc.cryptovault.app.feign.ConsoleRemoteService;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.UserDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TokenVO;
import com.mvc.cryptovault.common.bean.vo.UserSimpleVO;
import com.mvc.cryptovault.common.util.BaseContextHandler;
import com.mvc.cryptovault.common.util.JwtHelper;
import com.mvc.cryptovault.common.util.MessageConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.security.auth.login.LoginException;
import java.math.BigInteger;

@Service
public class UserService {

    @Autowired
    ConsoleRemoteService userRemoteService;
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
        TokenVO vo = new TokenVO();
        Result<AppUser> userResult = userRemoteService.getUserByUsername(userDTO.getUsername());
        AppUser user = userResult.getData();
        Assert.notNull(user, MessageConstants.getMsg("USER_PASS_WRONG"));
        Assert.isTrue(user.getStatus() == 1, MessageConstants.getMsg("ACCOUNT_LOCK"));
        Boolean passwordCheck = user.getPassword().equals(userDTO.getPassword());
        Assert.isTrue(passwordCheck, MessageConstants.getMsg("USER_PASS_WRONG"));
        String token = JwtHelper.createToken(userDTO.getUsername(), user.getId());
        String refreshToken = JwtHelper.createRefresh(userDTO.getUsername(), user.getId());
        vo.setRefreshToken(refreshToken);
        vo.setToken(token);
        vo.setUserId(user.getId());
        return vo;
    }

    public String refresh() throws LoginException {
        BigInteger userId = (BigInteger) BaseContextHandler.get("userId");
        String username = (String) BaseContextHandler.get("username");
        Result<AppUser> userResult = userRemoteService.getUserByUsername(username);
        AppUser user = userResult.getData();
        if (user.getStatus() == 0) {
            throw new LoginException();
        }
        return JwtHelper.createToken(username, userId);
    }

    public String getTag(BigInteger userId) {
        Result<String> result = userRemoteService.getTag(userId);
        return result.getData();
    }

    public Boolean getUserByCellphone(String cellphone) {
        Result<AppUser> userResult = userRemoteService.getUserByUsername(cellphone);
        return null != userResult.getData();
    }
}
