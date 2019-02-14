package com.mvc.cryptovault.dashboard.controller;

import com.mvc.cryptovault.common.bean.AppInfo;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.dashboard.service.AppService;
import com.mvc.cryptovault.dashboard.util.ReadUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @author qiyichen
 * @create 2019/2/13 15:02
 */
@Api(tags = "APP更新相关")
@RestController
@RequestMapping("app")
public class AppController extends BaseController {

    @Autowired
    AppService appService;

    @GetMapping("")
    @ApiOperation("获取app列表")
    public Result<List<AppInfo>> list() {
        return new Result<>(appService.getAppList());
    }

    @PostMapping("")
    @ApiOperation("设置当前更新信息")
    public Result<Boolean> readApp(@RequestBody MultipartFile file, @RequestParam String httpUrl) {
        String name = file.getOriginalFilename();
        AppInfo appInfo = null;
        name = name.toUpperCase();
        try {
            @Cleanup InputStream in = file.getInputStream();
            if (name.endsWith("APK")) {
                appInfo = ReadUtil.readAPK(in);
            } else {
                appInfo = ReadUtil.readIPA(in);
            }
            Assert.notNull(appInfo, "文件解析失败");
            appInfo.setHttpUrl(httpUrl);
            appService.saveApp(appInfo);
        } catch (Exception e) {
            return new Result<>(false);
        }
        return new Result<>(true);
    }

}
