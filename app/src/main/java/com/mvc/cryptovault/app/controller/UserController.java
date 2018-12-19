package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.service.SmsService;
import com.mvc.cryptovault.common.bean.dto.UserDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TokenVO;
import com.mvc.cryptovault.common.bean.vo.UserSimpleVO;
import com.mvc.cryptovault.common.permission.NotLogin;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import com.mvc.cryptovault.common.util.MessageConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    SmsService smsService;

    @ApiOperation("用户登录,缓存登录令牌.登录规则后续确定")
    @PostMapping("login")
    @SwaggerMock("${user.login}")
    @NotLogin
    public Result<TokenVO> login(HttpServletResponse response, @RequestBody @Valid UserDTO userDTO) {
        Boolean result = smsService.checkSmsValiCode(userDTO.getUsername(), userDTO.getValidCode());
        Assert.isTrue(result, MessageConstants.getMsg("SMS_ERROR"));
        TokenVO vo = userService.login(userDTO);
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

    @ApiOperation("获取登录验证码,1分钟1个手机号只能获取1次")
    @GetMapping("sms")
    @NotLogin
    public Result<Boolean> getSms(@RequestParam String cellphone) {
        Boolean exist = userService.getUserByCellphone(cellphone);
        if (exist) {
            //只有存在的账户才发送
            smsService.getSmsValiCode(cellphone);
        }
        return new Result<>(true);
    }

}
