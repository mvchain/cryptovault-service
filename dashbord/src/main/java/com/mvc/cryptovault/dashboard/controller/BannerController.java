package com.mvc.cryptovault.dashboard.controller;

import com.mvc.cryptovault.common.bean.AppBanner;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.dashboard.service.BannerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@Api(tags = "banner相关")
@RestController
@RequestMapping("banner")
public class BannerController extends BaseController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation("获取banner列表,不分页, 内容过多时需要删除")
    @GetMapping()
    public Result<List<AppBanner>> getList() {
        List<AppBanner> list = bannerService.bannerList();
        return new Result<>(list);
    }

    @DeleteMapping("{id}")
    public Result<Boolean> delBanner(@PathVariable BigInteger id) {
        bannerService.deleteBanner(id);
        return new Result<>(true);
    }

    @PostMapping
    public Result<Boolean> saveBanner(@RequestBody AppBanner appBanner) {
        bannerService.saveBanner(appBanner);
        return new Result<>(true);
    }

}
