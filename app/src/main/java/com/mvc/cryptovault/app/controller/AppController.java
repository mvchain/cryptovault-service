package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.service.AppService;
import com.mvc.cryptovault.common.bean.AppInfo;
import com.mvc.cryptovault.common.bean.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qiyichen
 * @create 2019/2/13 15:24
 */
@Api(tags = "App更新相关")
@RequestMapping("app")
@RestController
public class AppController {

    @Autowired
    AppService appService;

    @ApiOperation("获取当前最新app版本(APK或IPA,不区分大小写)")
    @GetMapping()
    public @ResponseBody
    Result<AppInfo> getApp(@RequestParam("appType") String appType) {
        return new Result<>(appService.getApp(appType));
    }

}
