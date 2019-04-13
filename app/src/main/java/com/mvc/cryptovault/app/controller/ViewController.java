package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.service.AppService;
import com.mvc.cryptovault.common.bean.AppBanner;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.permission.NotLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

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

    @ApiOperation("获取banner列表,不分页, 内容过多时需要删除")
    @GetMapping("banner")
    public Result<List<AppBanner>> getList() {
        List<AppBanner> list = appService.bannerList();
        return new Result<>(list);
    }
}
