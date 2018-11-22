package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectVO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AppProjectService extends AbstractService<AppProject> implements BaseService<AppProject> {
    public void newProject(DProjectDTO dProjectDTO) {
        AppProject appProject = new AppProject();
        BeanUtils.copyProperties(dProjectDTO, appProject);
        save(appProject);
        String key = "AppProject".toUpperCase() + "_" + dProjectDTO.getId();
        redisTemplate.opsForValue().set(key, JSON.toJSONString(appProject), 24, TimeUnit.HOURS);

    }

    public PageInfo<DProjectVO> projects(PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageSize(), pageDTO.getPageNum());
        List<AppProject> list = findAll();
        PageInfo result = new PageInfo(list);
        List<DProjectVO> vos = new ArrayList<>(list.size());
        for (AppProject appProject : list) {
            DProjectVO vo = new DProjectVO();
            BeanUtils.copyProperties(appProject, vo);
            vos.add(vo);
        }
        result.setList(vos);
        return result;
    }
}