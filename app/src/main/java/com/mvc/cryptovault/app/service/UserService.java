package com.mvc.cryptovault.app.service;

import com.mvc.cryptovault.app.feign.ConsoleRemoteService;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.*;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.security.auth.login.LoginException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    @Autowired
    ConsoleRemoteService userRemoteService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Autowired
    MailService mailService;

    public UserSimpleVO getUserById(BigInteger userId) {
        UserSimpleVO vo = new UserSimpleVO();
        Result<AppUser> userResult = userRemoteService.getUserById(userId);
        AppUser user = userResult.getData();
        vo.setNickname(user.getNickname());
        vo.setUsername(mailService.getMail(user.getEmail()));
        return vo;
    }

    public TokenVO login(UserDTO userDTO) {
        TokenVO vo = new TokenVO();
        Result<AppUser> userResult = userRemoteService.getUserByUsername(userDTO.getUsername());
        AppUser user = userResult.getData();
        Assert.notNull(user, MessageConstants.getMsg("USER_PASS_WRONG"));
        Assert.isTrue(user.getStatus() == 1 || user.getStatus() == 4, MessageConstants.getMsg("ACCOUNT_LOCK"));
        Boolean passwordCheck = user.getPassword().equals(userDTO.getPassword());
        Assert.isTrue(passwordCheck, MessageConstants.getMsg("USER_PASS_WRONG"));
        if (user.getStatus() == 4) {
            List<String> list = MnemonicUtil.getWordsList(user.getPvKey());
            Collections.shuffle(list);
            throw new PvkeyException(StringUtils.join(list, ","));
        }
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
        if(null == user){
            return null;
        }
        if (user.getStatus() == 0) {
            throw new LoginException();
        }
        return JwtHelper.createToken(username, userId);
    }

    public String getTag(BigInteger userId) {
        Result<String> result = userRemoteService.getTag(userId);
        return result.getData();
    }

    public AppUser getUserByUsername(String cellphone) {
        Result<AppUser> userResult = userRemoteService.getUserByUsername(cellphone);
        return userResult.getData();
    }

    public AppUserRetVO register(AppUserDTO appUserDTO) {
        Result<AppUserRetVO> userResult = userRemoteService.register(appUserDTO);
        return userResult.getData();
    }

    public void mnemonicsActive(String email) {
        userRemoteService.mnemonicsActive(email);
    }

    public void forget(BigInteger userId, String password) {
        userRemoteService.forget(userId, password);
    }

    public AppUser reset(AppUserResetDTO appUserResetDTO) {
        AppUser user = null;
        if (appUserResetDTO.getResetType() == 0) {
            Boolean result = mailService.checkSmsValiCode(appUserResetDTO.getEmail(), appUserResetDTO.getValue());
            Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
            user = userRemoteService.getUserByUsername(appUserResetDTO.getEmail()).getData();
        } else if (appUserResetDTO.getResetType() == 1) {
            user = userRemoteService.getUserByPvKey(appUserResetDTO.getValue()).getData();
        } else if (appUserResetDTO.getResetType() == 2) {
            String pvKey = MnemonicUtil.getPvKey(Arrays.asList(appUserResetDTO.getValue().split(",")));
            Assert.notNull(pvKey, MessageConstants.getMsg("USER_PASS_WRONG"));
            user = userRemoteService.getUserByPvKey(pvKey).getData();
        }
        Assert.notNull(user, MessageConstants.getMsg("USER_PASS_WRONG"));
        return user;
    }

    public void updatePwd(BigInteger userId, AppUserPwdDTO appUserPwdDTO) {
        AppUser user = userRemoteService.getUserById(userId).getData();
        Assert.isTrue(user.getPassword().equals(appUserPwdDTO.getPassword()), MessageConstants.getMsg("USER_PASS_WRONG"));
        user = new AppUser();
        user.setId(userId);
        user.setPassword(appUserPwdDTO.getNewPassword());
        userRemoteService.updateUser(user);
    }

    public void updateTransPwd(BigInteger userId, AppUserPwdDTO appUserPwdDTO) {
        AppUser user = userRemoteService.getUserById(userId).getData();
        Assert.isTrue(user.getTransactionPassword().equals(appUserPwdDTO.getPassword()), MessageConstants.getMsg("USER_PASS_WRONG"));
        user = new AppUser();
        user.setId(userId);
        user.setTransactionPassword(appUserPwdDTO.getNewPassword());
        userRemoteService.updateUser(user);
    }

    public void updateEmail(BigInteger userId, String email) {
        AppUser user = new AppUser();
        user.setId(userId);
        user.setEmail(email);
        userRemoteService.updateUser(user);
    }

    public List<RecommendVO> getRecommend(RecommendDTO dto) {
        if (dto.getInviteUserId() == null || dto.getInviteUserId().equals(BigInteger.ZERO)) {
            dto.setInviteUserId(BigInteger.valueOf(Integer.MAX_VALUE));
        }
        return userRemoteService.getRecommend(dto).getData();
    }

    public Boolean sign(BigInteger userId) {
        return  userRemoteService.sign(userId).getData();
    }

    public Boolean getSign(BigInteger userId) {
        return  userRemoteService.getSign(userId).getData();
    }
}
