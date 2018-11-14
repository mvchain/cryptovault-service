package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.common.bean.dto.UserDTO;
import com.mvc.cryptovault.common.bean.vo.TokenVO;
import com.mvc.cryptovault.common.bean.vo.UserSimpleVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("用户登录,缓存登录令牌.登录规则后续确定")
    @PostMapping("login")
    @SwaggerMock("${user.login}")
    public Result<Boolean> login(HttpServletResponse response, @RequestBody @Valid UserDTO userDTO) {
        TokenVO vo = userService.login(userDTO);
        return new Result(vo);
    }


    @ApiOperation("用户信息获取")
    @GetMapping("info")
    @SwaggerMock("${user.info}")
    public Result<UserSimpleVO> getInfo() {
        UserSimpleVO user = userService.getUserById(getUserId());
        return new Result<>(user);
    }


}
