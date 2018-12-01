package com.mvc.cryptovault.console.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.service.AppProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiyichen
 * @create 2018/11/13 17:05
 */
@RestController
@RequestMapping("appProject")
public class AppProjectController extends BaseController {

    @Autowired
    AppProjectService appProjectService;

    @GetMapping()
    Result<PageInfo<AppProject>> getProject(@RequestParam Integer projectType, @RequestParam(required = false) BigInteger id, @RequestParam Integer type, @RequestParam Integer pageSize) {
        final BigInteger projectId = id == null ? BigInteger.ZERO : id;
        PageHelper.orderBy("id desc");
        List<AppProject> list = appProjectService.findAll();
        list = list.stream().filter(obj -> obj.getVisiable() == 1).collect(Collectors.toList());
        if (null != type && type.equals(BusinessConstant.SEARCH_DIRECTION_UP)) {
            list = list.stream().filter(obj -> obj.getId().compareTo(projectId) > 0).collect(Collectors.toList());
        } else if (null != type && type.equals(BusinessConstant.SEARCH_DIRECTION_DOWN)) {
            list = list.stream().filter(obj -> obj.getId().compareTo(projectId) < 0).collect(Collectors.toList());
        }
        if (null != projectType) {
            list = list.stream().filter(obj -> obj.getStatus().equals(projectType)).collect(Collectors.toList());
        }
        list = list.stream().limit(pageSize).collect(Collectors.toList());
        return new Result<>(new PageInfo<>(list));
    }

    @GetMapping("{id}")
    Result<AppProject> getProjectById(@PathVariable BigInteger id) {
        AppProject result = appProjectService.findById(id);
        return new Result<>(result);
    }
}
