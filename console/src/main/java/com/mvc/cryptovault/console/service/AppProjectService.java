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
import com.mvc.cryptovault.console.util.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AppProjectService extends AbstractService<AppProject> implements BaseService<AppProject> {

    @Autowired
    CommonPairService commonPairService;
    public void newProject(DProjectDTO dProjectDTO) {
        BigInteger pairId  = commonPairService.findByTokenId(dProjectDTO.getBaseTokenId(), dProjectDTO.getTokenId());
        AppProject appProject = new AppProject();
        BeanUtils.copyProperties(dProjectDTO, appProject);
        appProject.setPairId(pairId);
        appProject.setStatus(0);
        save(appProject);
        String key = "AppProject".toUpperCase() + "_" + dProjectDTO.getId();
        redisTemplate.opsForValue().set(key, JSON.toJSONString(appProject), 24, TimeUnit.HOURS);

    }

    public PageInfo<DProjectVO> projects(PageDTO pageDTO) {
        PageHelper.startPage(pageDTO.getPageSize(), pageDTO.getPageNum(), "id desc");
        List<AppProject> list = findAll("id desc");
        Integer total = list.size();
        list = PageUtil.subList(list, pageDTO);
        PageInfo result = new PageInfo(list);
        List<DProjectVO> vos = new ArrayList<>(list.size());
        for (AppProject appProject : list) {
            DProjectVO vo = new DProjectVO();
            BeanUtils.copyProperties(appProject, vo);
            vos.add(vo);
        }
        result.setTotal(total);
        result.setList(vos);
        return result;
    }
}