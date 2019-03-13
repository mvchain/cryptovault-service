package com.mvc.cryptovault.dashboard.controller;

import com.mvc.cryptovault.common.bean.ExplorerBlockSetting;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.dashboard.service.ExplorerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qiyichen
 * @create 2019/3/13 17:04
 */
@RestController
@RequestMapping("explorer")
@Api(tags = "区块浏览器相关")
public class ExplorerController extends BaseController {

    @Autowired
    ExplorerService explorerService;

    @GetMapping()
    @ApiOperation("查询设置信息")
    public Result<ExplorerBlockSetting> getSetting() {
        return new Result<>(explorerService.getExplorerSetting());
    }

    @PostMapping()
    @ApiOperation("编辑设置信息")
    public Result<Boolean> setSetting(@RequestBody ExplorerBlockSetting setting) {
        explorerService.save(setting);
        return new Result<>(true);
    }

}
