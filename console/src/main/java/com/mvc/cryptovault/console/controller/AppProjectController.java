package com.mvc.cryptovault.console.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiyichen
 * @create 2018/11/13 17:05
 */
@RestController
@RequestMapping("appProject")
public class AppProjectController extends BaseController {

    @GetMapping()
    Result<PageInfo<AppProject>> getProject(@RequestParam Integer projectType, @RequestParam BigInteger id, @RequestParam Integer type) {
        final BigInteger projectId = id == null ? BigInteger.ZERO : id;
        List<AppProject> list = appProjectService.findAll();
        Stream<AppProject> stream = list.stream();
        if (null != type && type.equals(BusinessConstant.SEARCH_DIRECTION_UP)) {
            stream = stream.filter(obj -> obj.getId().compareTo(projectId) > 0);
        } else if (null != type && type.equals(BusinessConstant.SEARCH_DIRECTION_DOWN)) {
            stream = stream.filter(obj -> obj.getId().compareTo(projectId) < 0);
        }
        if (null != projectType) {
            stream = stream.filter(obj -> obj.getStatus().equals(projectType));
        }
        list = stream.collect(Collectors.toList());
        return new Result<>(new PageInfo<>(list));
    }

    @GetMapping("{id}")
    Result<AppProject> getProjectById(@PathVariable BigInteger id) {
        AppProject result = appProjectService.findById(id);
        return new Result<>(result);
    }
}
