package com.mvc.cryptovault.app.service;

import com.mvc.cryptovault.app.feign.ConsoleRemoteService;
import com.mvc.cryptovault.app.util.GoogleAuthUtil;
import com.mvc.cryptovault.app.util.GoogleRegInfo;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.*;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.common.util.*;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.security.auth.login.LoginException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        Assert.notNull(user, MessageConstants.getMsg("USER_PASS_WRONG"));
        vo.setNickname(user.getNickname());
        vo.setUsername(mailService.getMail(user.getEmail()));
        vo.setInviteCode(InviteUtil.toSerialCode(userId.longValue()));
        return vo;
    }

    public TokenVO login(UserDTO userDTO) {
        TokenVO vo = new TokenVO();
        String redisKey = "LOGIN_WRONG_TIME_" + userDTO.getUsername();
        checkWrongTimes(redisKey, userDTO.getImageToken(), userDTO.getUsername());
        Result<AppUser> userResult = userRemoteService.getUserByUsername(userDTO.getUsername());
        AppUser user = userResult.getData();
        wrongLogin(null != user, redisKey);
        Assert.notNull(user, MessageConstants.getMsg("USER_PASS_WRONG"));
        Assert.isTrue(user.getStatus() == 1 || user.getStatus() == 4, MessageConstants.getMsg("ACCOUNT_LOCK"));
        Boolean passwordCheck = user.getPassword().equals(userDTO.getPassword());
        wrongLogin(passwordCheck, redisKey);
//        if (user.getStatus() == 4) {
//            List<String> list = MnemonicUtil.getWordsList(user.getPvKey());
//            Collections.shuffle(list);
//            throw new PvkeyException(StringUtils.join(list, ","));
//        }
        Integer googleCheck = null == user.getGoogleCheck() || 0 == user.getGoogleCheck() ? 1 : 0;
        Assert.isTrue(passwordCheck, MessageConstants.getMsg("USER_PASS_WRONG"));
        String token = JwtHelper.createToken(userDTO.getUsername(), user.getId(), googleCheck);
        String refreshToken = JwtHelper.createRefresh(userDTO.getUsername(), user.getId(), googleCheck);
        //密码正确后清空错误次数
        redisTemplate.delete(redisKey);
        vo.setRefreshToken(refreshToken);
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setEmail(user.getEmail());
        vo.setPublicKey(user.getPublicKey());
        vo.setGoogleCheck(user.getGoogleCheck());
        return vo;
    }

    private Boolean checkWrongTimes(String redisKey, String imageToken, String email) {
        Long wrongTimes = redisTemplate.opsForHash().increment(redisKey, "TIME", 0);
        if (null == wrongTimes || wrongTimes.compareTo(5L) < 0) {
            return true;
        }
        Claims claim = JwtHelper.parseJWT(imageToken);
        if (null == claim) {
            throw new PassWrongMoreException(MessageConstants.getMsg("USER_PASS_WRONG_MORE"), 402);
        }
        String username = claim.get("username", String.class);
        String type = claim.get("type", String.class);
        if (!type.equalsIgnoreCase("valiCode") || !username.equalsIgnoreCase(email)) {
            throw new PassWrongMoreException(MessageConstants.getMsg("USER_PASS_WRONG_MORE"), 402);
        }
        return true;
    }

    private void wrongLogin(Boolean passwordCheck, String redisKey) {
        if (!passwordCheck) {
            //从第一次错误时间开始计时,10分钟内每次错误则错误次数加1
            Long wrongTimes = redisTemplate.opsForHash().increment(redisKey, "TIME", 1);
            if (wrongTimes.equals(1L)) {
                redisTemplate.expire(redisKey, 10, TimeUnit.MINUTES);
            }
            //超过5次错误则抛出错误,下一次需要输入验证码
            if (wrongTimes.compareTo(5L) >= 0) {
                redisTemplate.expire(redisKey, 1, TimeUnit.HOURS);
                throw new PassWrongMoreException(MessageConstants.getMsg("USER_PASS_WRONG_MORE"), 402);
            }
        }
    }

    public String refresh() throws LoginException {
        BigInteger userId = (BigInteger) BaseContextHandler.get("userId");
        String username = (String) BaseContextHandler.get("username");
        Result<AppUser> userResult = userRemoteService.getUserByUsername(username);
        AppUser user = userResult.getData();
        if (null == user) {
            return null;
        }
        if (user.getStatus() == 0) {
            throw new LoginException();
        }
        return JwtHelper.createToken(username, userId, 1);
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

    public void forget(BigInteger userId, String password, Integer type) {
        userRemoteService.forget(userId, password, type);
    }

    public AppUser reset(AppUserResetDTO appUserResetDTO) {
        AppUser user = null;
        if (appUserResetDTO.getResetType() == 0) {
            Boolean result = mailService.checkSmsValiCode(appUserResetDTO.getEmail(), appUserResetDTO.getValue());
            Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
            user = userRemoteService.getUserByUsername(appUserResetDTO.getEmail()).getData();
            Assert.notNull(user, MessageConstants.getMsg("SMS_ERROR"));
        } else if (appUserResetDTO.getResetType() == 1) {
            user = userRemoteService.getUserByPvKey(appUserResetDTO.getValue()).getData();
            Assert.notNull(user, MessageConstants.getMsg("PVKEY_WRONG"));
        } else if (appUserResetDTO.getResetType() == 2) {
            String pvKey = MnemonicUtil.getPvKey(Arrays.asList(appUserResetDTO.getValue().split(",")));
            Assert.notNull(pvKey, MessageConstants.getMsg("MNEMONICS_ERROR"));
            user = userRemoteService.getUserByPvKey(pvKey).getData();
            Assert.notNull(user, MessageConstants.getMsg("MNEMONICS_ERROR"));
        }
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
        return userRemoteService.sign(userId).getData();
    }

    public Boolean getSign(BigInteger userId) {
        return userRemoteService.getSign(userId).getData();
    }

    public String getEmail(BigInteger userId) {
        Result<AppUser> userResult = userRemoteService.getUserById(userId);
        AppUser user = userResult.getData();
        return user.getEmail();
    }

    public AppUser getUser(BigInteger userId) {
        return userRemoteService.getUserById(userId).getData();
    }

    public GoogleRegInfo createGoogleInfo(AppUser user) {
        GoogleRegInfo info = GoogleAuthUtil.createCredentials(user.getEmail(), user.getGoogleSecret());
        user.setGoogleSecret(info.getSecret());
        Result<Boolean> result = userRemoteService.updateUser(user);
        if (result.getData() == false) {
            return null;
        }
        return info;
    }

    public TokenVO updateTokenSwitch(BigInteger userId, GoogleSetVO googleSetVO) {
        Result<AppUser> data = userRemoteService.getUserById(userId);
        AppUser user = data.getData();
        if (null == user || StringUtils.isBlank(user.getGoogleSecret())) {
            return null;
        }
        Assert.isTrue(null != googleSetVO.getPassword() && user.getPassword().equals(googleSetVO.getPassword()), MessageConstants.getMsg("USER_PASS_WRONG"));
        Boolean checkResult = GoogleAuthUtil.checkUser(user.getGoogleSecret(), googleSetVO.getGoogleCode());
        Assert.isTrue(checkResult, MessageConstants.getMsg("SMS_ERROR"));
        user.setGoogleCheck(1);
        userRemoteService.updateUser(user);
        TokenVO tokenVO = getTokenVO(userId, user);
        return tokenVO;
    }

    private TokenVO getTokenVO(BigInteger userId, AppUser user) {
        String token = JwtHelper.createToken(user.getEmail(), userId, 1);
        String refreshToken = JwtHelper.createRefresh(user.getEmail(), userId, 1);
        TokenVO tokenVO = new TokenVO();
        tokenVO.setPublicKey(user.getPublicKey());
        tokenVO.setEmail(user.getEmail());
        tokenVO.setUserId(userId);
        tokenVO.setRefreshToken(refreshToken);
        tokenVO.setToken(token);
        tokenVO.setGoogleCheck(user.getGoogleCheck());
        return tokenVO;
    }

    public TokenVO checkGoogleCode(BigInteger userId, Integer googleCode) {
        Result<AppUser> data = userRemoteService.getUserById(userId);
        AppUser user = data.getData();
        if (null == user || StringUtils.isBlank(user.getGoogleSecret())) {
            return null;
        }
        Boolean checkResult = GoogleAuthUtil.checkUser(user.getGoogleSecret(), googleCode);
        Assert.isTrue(checkResult, MessageConstants.getMsg("SMS_ERROR"));
        return getTokenVO(userId, user);
    }

    public String getSalt(BigInteger userId) {
        Result<AppUser> userResult = userRemoteService.getUserById(userId);
        AppUser user = userResult.getData();
        return user.getSalt();
    }

}
