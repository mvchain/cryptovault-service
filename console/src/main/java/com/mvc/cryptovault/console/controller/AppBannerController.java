package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.AppBanner;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/13 17:05
 */
@RestController
@RequestMapping("banner")
public class AppBannerController extends BaseController {

    @Autowired
    AppBannerService appBannerService;

    @GetMapping("")
    public Result<List<AppBanner>> bannerList() {
        List<AppBanner> list = appBannerService.findAll("id desc");
        return new Result<>(list);
    }

    @DeleteMapping("{id}")
    public Result<Boolean> delBanner(@PathVariable("id") BigInteger id) {
        appBannerService.deleteById(id);
        appBannerService.updateAllCache("id desc");
        appBannerService.updateCache(id);
        return new Result<>(true);
    }

    @PostMapping("")
    public Result<Boolean> saveBanner(@RequestBody AppBanner appBanner) {
        if (appBanner.getId() == null) {
            appBanner.setCreatedAt(System.currentTimeMillis());
            appBannerService.save(appBanner);
        } else {
            appBanner.setCreatedAt(null);
            appBannerService.update(appBanner);
        }
        appBannerService.updateAllCache("id desc");
        appBannerService.updateCache(appBanner.getId());
        return new Result<>(true);

    }

}
