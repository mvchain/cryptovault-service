package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.service.MailService;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.*;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.permission.NotLogin;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import com.mvc.cryptovault.common.util.InviteUtil;
import com.mvc.cryptovault.common.util.JwtHelper;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.common.util.MnemonicUtil;
import io.jsonwebtoken.Claims;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 用户相关
 *
 * @author qiyichen
 * @create 2018/11/6 19:02
 */
@Api(tags = "用户相关")
@RequestMapping("user")
@RestController
public class UserController extends BaseController {

    @Autowired
    MailService mailService;

    @ApiOperation("用户登录,缓存登录令牌.登录规则后续确定,如返回406则需要校验助记词(错误信息中返回助记词字符串)")
    @PostMapping("login")
    @SwaggerMock("${user.login}")
    @NotLogin
    public Result<TokenVO> login(@RequestBody @Valid UserDTO userDTO) {
        String key = RedisConstant.MAIL_VALI_PRE + userDTO.getUsername();
        Boolean result = mailService.checkSmsValiCode(userDTO.getUsername(), userDTO.getValidCode());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        TokenVO vo = userService.login(userDTO);
        redisTemplate.delete(key);
        return new Result(vo);
    }

    @PostMapping("refresh")
    @ApiOperation("刷新令牌")
    Result<String> refresh() throws LoginException {
        String result = userService.refresh();
        return new Result(result);
    }

    @ApiOperation("用户信息获取")
    @GetMapping("info")
    @SwaggerMock("${user.info}")
    public Result<UserSimpleVO> getInfo() {
        UserSimpleVO user = userService.getUserById(getUserId());
        return new Result<>(user);
    }

    @ApiOperation("用户分组信息获取,用户推送设置(tag),内容为英文逗号分隔开的id列表")
    @GetMapping("tag")
    public Result<String> getTag() {
        String result = userService.getTag(getUserId());
        return new Result<>(result);
    }

    @ApiOperation("获取邮箱验证码, 5分钟内有效, 以最后一次为准")
    @GetMapping("email/logout")
    @NotLogin
    public Result<Boolean> getSms(@RequestParam String email) {
        mailService.send(email);
        return new Result<>(true);
    }

    @ApiOperation("校验助记词并激活用户")
    @PostMapping("mnemonics")
    @NotLogin
    public Result<TokenVO> mnemonicsCheck(@RequestBody MnemonicsDTO mnemonicsDTO) {
        AppUser user = userService.getUserByUsername(mnemonicsDTO.getEmail());
        Assert.notNull(user, MessageConstants.getMsg("USER_PASS_WRONG"));
        Assert.isTrue(user.getStatus() == 4, MessageConstants.getMsg("USER_ACTIVE"));
        Boolean result = MnemonicUtil.equals(user.getPvKey(), Arrays.asList(mnemonicsDTO.getMnemonics().split(",")));
        Assert.isTrue(result, MessageConstants.getMsg("MNEMONICS_ERROR"));
        userService.mnemonicsActive(mnemonicsDTO.getEmail());
        TokenVO vo = new TokenVO();
        String token = JwtHelper.createToken(mnemonicsDTO.getEmail(), user.getId());
        String refreshToken = JwtHelper.createRefresh(mnemonicsDTO.getEmail(), user.getId());
        vo.setRefreshToken(refreshToken);
        vo.setToken(token);
        vo.setUserId(user.getId());
        return new Result<>(vo);
    }

    @ApiOperation("校验注册信息,返回一个一次性的token,需要在后续注册时传入")
    @NotLogin
    @PostMapping("")
    public Result<String> regCheck(@RequestBody AppuserRegCheckDTO appuserRegCheckDTO) {
        String key = RedisConstant.MAIL_VALI_PRE + appuserRegCheckDTO.getEmail();
        Boolean result = mailService.checkSmsValiCode(appuserRegCheckDTO.getEmail(), appuserRegCheckDTO.getValiCode());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        AppUser user = userService.getUserByUsername(appuserRegCheckDTO.getEmail());
        Assert.isTrue(null == user, MessageConstants.getMsg("USER_EXIST"));
        Long id = InviteUtil.codeToId(appuserRegCheckDTO.getInviteCode());
        Assert.isTrue(null != id && !id.equals(0L), MessageConstants.getMsg("INVITE_ERROR"));
        UserSimpleVO vo = userService.getUserById(BigInteger.valueOf(id));
        Assert.isTrue(null != vo, MessageConstants.getMsg("INVITE_ERROR"));
        String tempToken = JwtHelper.createReg(appuserRegCheckDTO.getEmail(), BigInteger.valueOf(id));
        redisTemplate.delete(key);
        return new Result<>(tempToken);
    }

    @ApiOperation("用户注册,需要将之前的信息和token一起带入进行校验")
    @NotLogin
    @PostMapping("register")
    public Result<AppUserRetVO> register(@RequestBody AppUserDTO appUserDTO) {
        Claims claim = JwtHelper.parseJWT(appUserDTO.getToken());
        Assert.notNull(claim, MessageConstants.getMsg("TOKEN_EXPIRE"));
        String username = claim.get("username", String.class);
        String type = claim.get("type", String.class);
        Assert.isTrue("reg".equals(type), MessageConstants.getMsg("TOKEN_EXPIRE"));
        BigInteger userId = null;
        try {
            userId = claim.get("userId", BigInteger.class);
        } catch (Exception e) {
            Integer userIdInt = claim.get("userId", Integer.class);
            userId = BigInteger.valueOf(userIdInt);
        }
        Long id = InviteUtil.codeToId(appUserDTO.getInviteCode());
        Boolean checkResult = userId.longValue() == id && appUserDTO.getEmail().equals(username);
        Assert.isTrue(id != 0L, MessageConstants.getMsg("INVITE_ERROR"));
        Assert.isTrue(checkResult, MessageConstants.getMsg("REGISTER_WRONG"));
        AppUserRetVO vo = userService.register(appUserDTO);
        return new Result<>(vo);
    }

    @ApiOperation("获取邀请码")
    @GetMapping("invitation")
    public Result<String> getInvitation() {
        BigInteger userId = getUserId();
        String code = InviteUtil.toSerialCode(userId.longValue());
        return new Result<>(code);
    }

    @ApiOperation("获取助记词(打乱)")
    @GetMapping("mnemonics")
    @NotLogin
    public Result<List<String>> getInvitation(@RequestParam String email) {
        AppUser appUser = userService.getUserByUsername(email);
        Assert.notNull(appUser, MessageConstants.getMsg("USER_PASS_WRONG"));
        List<String> list = MnemonicUtil.getWordsList(appUser.getPvKey());
        Collections.shuffle(list);
        return new Result<>(list);
    }

    @ApiOperation("重置密码,返回一次性token")
    @PostMapping("reset")
    @NotLogin
    public Result<String> reset(@RequestBody AppUserResetDTO appUserResetDTO) {
        AppUser user = userService.reset(appUserResetDTO);
        String tempToken = JwtHelper.createForget(user.getEmail(), user.getId());
        return new Result<>(tempToken);
    }

    @ApiOperation("忘记密码(修改密码)")
    @NotLogin
    @PutMapping("forget")
    public Result<Boolean> forget(@RequestBody PasswordDTO passwordDTO) {
        Claims claim = JwtHelper.parseJWT(passwordDTO.getToken());
        Assert.notNull(claim, MessageConstants.getMsg("TOKEN_EXPIRE"));
        String type = claim.get("type", String.class);
        Assert.isTrue("forget".equals(type), MessageConstants.getMsg("TOKEN_EXPIRE"));
        BigInteger userId = null;
        try {
            userId = claim.get("userId", BigInteger.class);
        } catch (Exception e) {
            Integer userIdInt = claim.get("userId", Integer.class);
            userId = BigInteger.valueOf(userIdInt);
        }
        userService.forget(userId, passwordDTO.getPassword());
        return new Result<>(true);
    }

    @ApiOperation("登录密码修改")
    @PutMapping("password")
    public Result<Boolean> updatePwd(@RequestBody AppUserPwdDTO appUserPwdDTO) {
        userService.updatePwd(getUserId(), appUserPwdDTO);
        return new Result<>(true);
    }

    @ApiOperation("支付密码修改")
    @PutMapping("transactionPassword")
    public Result<Boolean> updateTransPwd(@RequestBody AppUserPwdDTO appUserPwdDTO) {
        userService.updateTransPwd(getUserId(), appUserPwdDTO);
        return new Result<>(true);
    }

    @ApiOperation("修改绑定邮箱")
    @PutMapping("email")
    public Result<Boolean> updateEmail(@RequestBody AppUserMailDTO appUserMailDTO) {
        Boolean result = mailService.checkSmsValiCode(appUserMailDTO.getEmail(), appUserMailDTO.getValiCode());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        Claims claim = JwtHelper.parseJWT(appUserMailDTO.getToken());
        String type = claim.get("type", String.class);
        Assert.isTrue("email".equals(type), MessageConstants.getMsg("TOKEN_EXPIRE"));
        BigInteger userId = null;
        try {
            userId = claim.get("userId", BigInteger.class);
        } catch (Exception e) {
            Integer userIdInt = claim.get("userId", Integer.class);
            userId = BigInteger.valueOf(userIdInt);
        }
        Assert.isTrue(userId.equals(getUserId()), MessageConstants.getMsg("TOKEN_EXPIRE"));
        userService.updateEmail(getUserId(), appUserMailDTO.getEmail());
        return new Result<>(true);
    }

    @ApiOperation("校验邮箱状态(修改密码时第一步校验)")
    @PostMapping("email")
    public Result<String> checkEmail(@RequestBody AppUserEmailDTO appUserEmailDTO) {
        String email = userService.getUserById(getUserId()).getUsername();
        Boolean result = mailService.checkSmsValiCode(email, appUserEmailDTO.getValiCode());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        String token = JwtHelper.create(email, getUserId(), "email");
        return new Result<>(token);
    }

    @ApiOperation("发送验证码(不输入邮箱地址,直接取当前用户注册邮箱)")
    @GetMapping(value = "email", headers = "Authorization")
    public Result<Boolean> getEmail() {
        String email = userService.getUserById(getUserId()).getUsername();
        mailService.send(email);
        return new Result<>(true);
    }


    @ApiOperation("获取推荐人列表,分页加载")
    @GetMapping("recommend")
    public Result<List<RecommendVO>> getRecommend(@ModelAttribute RecommendDTO recommendDTO) {
        recommendDTO.setUserId(getUserId());
        List<RecommendVO> result = userService.getRecommend(recommendDTO);
        return new Result<>(result);
    }

    @ApiOperation("用户签到")
    @PutMapping("sign")
    public Result<Boolean> sign() {
        return new Result<>(userService.sign(getUserId()));
    }

    @ApiOperation("获取用户是否已签到")
    @GetMapping("sign")
    public Result<Boolean> getSign() {
        return new Result<>(userService.getSign(getUserId()));
    }

}
