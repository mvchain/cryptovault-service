package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.common.bean.dto.UserDTO;
import com.mvc.cryptovault.common.bean.vo.TokenVO;
import com.mvc.cryptovault.common.bean.vo.UserSimpleVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.permission.NotLogin;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import com.mvc.cryptovault.common.util.BaseContextHandler;
import com.mvc.cryptovault.common.util.JwtHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.el.parser.Token;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigInteger;

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

    @ApiOperation("用户登录,缓存登录令牌.登录规则后续确定")
    @PostMapping("login")
    @SwaggerMock("${user.login}")
    @NotLogin
    public Result<TokenVO> login(HttpServletResponse response, @RequestBody @Valid UserDTO userDTO) {
        TokenVO vo = userService.login(userDTO);
        return new Result(vo);
    }

    @PostMapping("refresh")
    @ApiOperation("刷新令牌")
    @NotLogin
    Result<String> refresh() {
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


}
