package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.AppProjectUserTransaction;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.CommonPair;
import com.mvc.cryptovault.common.bean.dto.ImportPartake;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.ExportPartake;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectVO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppProjectMapper;
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
    @Autowired
    AppProjectUserTransactionService appProjectUserTransactionService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    AppProjectPartakeService appProjectPartakeService;
    @Autowired
    AppProjectMapper appProjectMapper;

    public void newProject(DProjectDTO dProjectDTO) {
        AppProject appProject = new AppProject();
        BeanUtils.copyProperties(dProjectDTO, appProject);
        appProject.setStatus(0);
        CommonPair pair = commonPairService.findByTokenId(dProjectDTO.getBaseTokenId(), dProjectDTO.getTokenId());
        appProject.setPairId(null == pair ? BigInteger.ZERO : pair.getId());
        appProject.setTokenName(null == pair ? "" : pair.getTokenName());
        appProject.setBaseTokenName(null == pair ? "" : pair.getBaseTokenName());
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

    public void importPartake(List<ImportPartake> list, String fileName) {
        String key = RedisConstant.PARTAKE_IMPORT + fileName;
        AppProject appProject = null;
        for (ImportPartake partake : list) {
            if (null == appProject) {
                appProject = findById(partake.getProjectId());
            }
            //修改成功的众筹
            appProjectPartakeService.savePartake(partake, appProject);
            appProjectUserTransactionService.updatePartake(partake, appProject);
        }
        //将剩余众筹标记为失败
        appProjectUserTransactionService.updateFailPartake(list.get(0).getProjectId(), appProject);
        redisTemplate.delete(key);
    }

    public List<ExportPartake> exportPartake(BigInteger id) {
        AppProject project = findById(id);
        if (null == project) {
            return null;
        }
        List<AppProjectUserTransaction> list = appProjectUserTransactionService.findBy("projectId", project.getId());
        List<ExportPartake> result = new ArrayList<>(list.size());
        list.forEach(trans -> {
            ExportPartake partake = new ExportPartake();
            AppUser user = appUserService.findById(trans.getUserId());
            if (null == user) {
                return;
            }
            partake.setValue(trans.getValue());
            partake.setUserId(trans.getUserId());
            partake.setTokenName(project.getTokenName());
            partake.setBaseTokenName(project.getBaseTokenName());
            partake.setCellphone(user.getCellphone());
            partake.setNickname(user.getNickname());
            partake.setProjectId(project.getId());
            partake.setProjectName(project.getProjectName());
            result.add(partake);
        });
        return result;
    }

    public void updateProjectStatus() {
        Long currentTimeMillis = System.currentTimeMillis();
        appProjectMapper.updateProjectStartStatus(currentTimeMillis);
        appProjectMapper.updateProjectStopStatus(currentTimeMillis);
    }
}