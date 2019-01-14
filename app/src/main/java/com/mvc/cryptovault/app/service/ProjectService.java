package com.mvc.cryptovault.app.service;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.app.feign.ConsoleRemoteService;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.dto.ProjectBuyDTO;
import com.mvc.cryptovault.common.bean.dto.ProjectDTO;
import com.mvc.cryptovault.common.bean.dto.ReservationDTO;
import com.mvc.cryptovault.common.bean.vo.ProjectBuyVO;
import com.mvc.cryptovault.common.bean.vo.ProjectSimpleVO;
import com.mvc.cryptovault.common.bean.vo.PurchaseVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ConsoleRemoteService projectRemoteService;

    public List<ProjectSimpleVO> getProject(ProjectDTO projectDTO) {
        Result<PageInfo<AppProject>> listData = projectRemoteService.getProject(projectDTO.getProjectType(), projectDTO.getProjectId(), projectDTO.getType(), projectDTO.getPageSize());
        List<ProjectSimpleVO> result = new ArrayList<>(listData.getData().getList().size());
        for (AppProject appProject : listData.getData().getList()) {
            ProjectSimpleVO vo = new ProjectSimpleVO();
            BeanUtils.copyProperties(appProject, vo);
            vo.setProjectId(appProject.getId());
            vo.setTokenId(appProject.getTokenId());
            vo.setTokenName(appProject.getTokenName());
            vo.setTotal(appProject.getProjectTotal());
            result.add(vo);
        }
        return result;
    }

    public List<PurchaseVO> getReservation(BigInteger userId, ReservationDTO reservationDTO) {
        Result<PageInfo<PurchaseVO>> listData = projectRemoteService.getReservation(userId, reservationDTO);
        return listData.getData().getList();
    }

    public ProjectBuyVO getPurchaseInfo(BigInteger userId, BigInteger id) {
        Result<ProjectBuyVO> data = projectRemoteService.getPurchaseInfo(userId, id);
        return data.getData();
    }

    public Boolean buy(BigInteger userId, BigInteger id, ProjectBuyDTO dto) {
        Result<Boolean> result = projectRemoteService.buy(userId, id, dto);
        return result.getData();
    }

}
