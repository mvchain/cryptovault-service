package com.mvc.cryptovault.app.controller;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.app.bean.dto.UserDTO;
import com.mvc.cryptovault.app.bean.vo.TransactionTokenVO;
import com.mvc.cryptovault.app.bean.vo.UserSimpleVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;

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

    public static void main(String[] args) {


        UserSimpleVO data = new UserSimpleVO();
        data.setNickname("人工智障");
        data.setUsername("188****8888");
        data.setHeadImage("https://upload.jianshu.io/users/upload_avatars/8818451/83d1f0a4-3574-42f6-9e94-aefc2306883a?imageMogr2/auto-orient/strip|imageView2/1/w/96/h/96");
        Result result = new Result();
        result.setMessage("OK");
        result.setCode(200);
        result.setData(data);
        System.out.printf(JSON.toJSONString(result));
    }

    @ApiOperation("用户登录,缓存登录令牌.登录规则后续确定")
    @PostMapping("login")
    @SwaggerMock("${user.login}")
    public Result<Boolean> login(HttpServletResponse response, @RequestBody @Valid UserDTO user) throws IOException, InterruptedException {
        return new Result(500, "服务器错误", null);
    }


    @ApiOperation("用户信息获取")
    @GetMapping("info")
    @SwaggerMock("${user.info}")
    public Result<UserSimpleVO> getInfo(){
        return mockResult;
    }


}
