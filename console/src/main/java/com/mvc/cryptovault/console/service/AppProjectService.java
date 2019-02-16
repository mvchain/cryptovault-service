package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.AppProjectUserTransaction;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.ImportPartake;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.ExportPartake;
import com.mvc.cryptovault.common.bean.vo.ProjectPublishDetailVO;
import com.mvc.cryptovault.common.bean.vo.ProjectPublishListVO;
import com.mvc.cryptovault.common.bean.vo.ProjectPublishVO;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectVO;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppProjectMapper;
import com.mvc.cryptovault.console.util.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

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
    CommonTokenService commonTokenService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    AppProjectPartakeService appProjectPartakeService;
    @Autowired
    AppProjectMapper appProjectMapper;
    @Autowired
    AppOrderService appOrderService;

    public void newProject(DProjectDTO dProjectDTO) {
        AppProject appProject = new AppProject();
        BeanUtils.copyProperties(dProjectDTO, appProject);
        appProject.setStatus(0);
        appProject.setPairId(BigInteger.ZERO);
        appProject.setTokenName(commonTokenService.getTokenName(dProjectDTO.getTokenId()));
        appProject.setBaseTokenName(commonTokenService.getTokenName(dProjectDTO.getBaseTokenId()));
        appProject.setCreatedAt(System.currentTimeMillis());
        appProject.setUpdatedAt(System.currentTimeMillis());
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
            String listKey = "AppProjectUserTransaction".toUpperCase() + "_USER_" + partake.getUserId();
            redisTemplate.delete(listKey);
            appProjectUserTransactionService.putAll(partake.getUserId(), true);
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
        Integer result1 = appProjectMapper.updateProjectStartStatus(currentTimeMillis);
        Condition condition = new Condition(AppProject.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "status = ", 1);
        if (result1 > 0) {
            updateAllCache("id desc");
        }
        ConditionUtil.andCondition(criteria, "stop_at <= ", currentTimeMillis);
        List<AppProject> list = findByCondition(condition);
        for (AppProject appProject : list) {
            Integer result = appProjectMapper.updateProjectStopStatus(appProject.getId(), appProject.getUpdatedAt(), currentTimeMillis);
            if (result < 0) {
                continue;
            }
            updateAllCache("id desc");
            //筛选众筹成功人员
            List<ImportPartake> partakes = appProjectPartakeService.getPartakes(appProject);
            importPartake(partakes, appProject.getId().toString());
        }

    }

    public String findIdsByName(String projectName) {
        return appProjectMapper.findIdsByName(projectName);
    }

    public List<AppProject> getMyProject(BigInteger userId, BigInteger id, Integer pageSize) {
        String str = "";
        PageHelper.clearPage();
        if (null != id && !id.equals(BigInteger.ZERO)) {
            str = " AND t2.project_id < " + id;
        }
        return appProjectMapper.getMyProject(userId, str, pageSize);
    }

    public List<ProjectPublishVO> getPublish(BigInteger userId, BigInteger id, PageDTO pageDTO) {
        return appProjectUserTransactionService.getPublish(userId, id, pageDTO);
    }

    public ProjectPublishDetailVO getPublishDetail(BigInteger userId, BigInteger projectId) {
        AppProject project = findById(projectId);
        if (null == project) {
            return null;
        }
        ProjectPublishDetailVO vo = appProjectUserTransactionService.getPublishDetail(userId, projectId);
        BeanUtils.copyProperties(project, vo);
        vo.setProjectId(project.getId());
        vo.setTokenId(project.getTokenId());
        vo.setTokenName(project.getTokenName());
        vo.setBaseTokenName(project.getBaseTokenName());
        vo.setTotal(project.getProjectTotal());
        return vo;
    }

    public List<ProjectPublishListVO> getPublishList(BigInteger userId, BigInteger projectId, BigInteger id, PageDTO pageDTO) {
        return appOrderService.getPublishList(userId, projectId, id, pageDTO);
    }

}