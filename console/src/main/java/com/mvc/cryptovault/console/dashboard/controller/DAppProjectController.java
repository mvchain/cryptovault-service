package com.mvc.cryptovault.console.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectVO;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppProjectService;
import com.mvc.cryptovault.console.service.AppProjectUserTransactionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * @author qiyichen
 * @create 2018/11/21 16:45
 */
@RestController
@RequestMapping("dashboard/appProject")
public class DAppProjectController extends BaseController {
    @Autowired
    AppProjectService appProjectService;
    @Autowired
    AppProjectUserTransactionService appProjectUserTransactionService;

    @DeleteMapping("{id}")
    public Result<Boolean> deleteProject(@PathVariable("id") BigInteger id) {
        var appProject = appProjectService.findById(id);
        if (null == appProject) {
            return new Result<>(true);
        }
        //只有未展示的项目才可以删除
        Assert.isTrue(null != appProject && appProject.getVisiable() == 0, "项目已展示,无法删除");
        //有订单的项目不能删
        Boolean result = appProjectUserTransactionService.existTrans(id);
        Assert.isTrue(!result, "已有交易无法删除");
        appProjectService.deleteById(id);
        appProjectService.updateAllCache("id desc");
        appProjectService.updateCache(id);
        return new Result<>(true);
    }

    @PutMapping("")
    public Result<Boolean> updateProject(@RequestBody DProjectDTO dProjectDTO) {
        AppProject appProject = new AppProject();
        BeanUtils.copyProperties(dProjectDTO, appProject);
        appProjectService.update(appProject);
        appProject = appProjectService.findById(dProjectDTO.getId());
        String key = "AppProject".toUpperCase() + "_" + dProjectDTO.getId();
        redisTemplate.opsForValue().set(key, JSON.toJSONString(appProject), 24, TimeUnit.HOURS);
        appProjectService.updateAllCache("id desc");
        appProjectService.updateCache(dProjectDTO.getId());
        return new Result<>(true);
    }

    @PostMapping("")
    public Result<Boolean> newProject(@RequestBody DProjectDTO dProjectDTO) {
        appProjectService.newProject(dProjectDTO);
        appProjectService.updateAllCache("id desc");
        return new Result<>(true);
    }

    @GetMapping("{id}")
    public Result<DProjectDetailVO> getDetail(@PathVariable("id") BigInteger id) {
        AppProject appProject = appProjectService.findById(id);
        DProjectDetailVO vo = new DProjectDetailVO();
        BeanUtils.copyProperties(appProject, vo);
        return new Result<>(vo);
    }

    @GetMapping("")
    public Result<PageInfo<DProjectVO>> projects(@ModelAttribute PageDTO pageDTO) {
        PageInfo<DProjectVO> result = appProjectService.projects(pageDTO);
        return new Result<>(result);
    }

}
