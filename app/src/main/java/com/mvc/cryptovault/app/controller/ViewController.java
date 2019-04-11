package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.service.AppService;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.permission.NotLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/2/13 15:24
 */
@Api(tags = "web view相关")
@RequestMapping("view")
@RestController
public class ViewController {

    @Autowired
    AppService appService;

    @ApiOperation("获取view")
    @GetMapping()
    @NotLogin
    public @ResponseBody
    Result<String> getView(@RequestParam("type") @ApiParam("1banner 2financial") Integer type, @RequestParam("id") BigInteger id) {
        return new Result<>(appService.getView(type, id));
    }

}
