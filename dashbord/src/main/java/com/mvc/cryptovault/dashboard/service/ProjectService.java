package com.mvc.cryptovault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectOrderDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectOrderVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class ProjectService extends BaseService {


    public PageInfo<DProjectVO> projects(PageDTO pageDTO) {
        Result<PageInfo<DProjectVO>> result = remoteService.projects(pageDTO);
        return result.getData();
    }

    public DProjectDetailVO getDetail(BigInteger id) {
        Result<DProjectDetailVO> result = remoteService.getDetail(id);
        return result.getData();
    }

    public Boolean newProject(DProjectDTO dProjectDTO) {
        Result<Boolean> result = remoteService.newProject(dProjectDTO);
        return result.getData();
    }

    public Boolean updateProject(DProjectDTO dProjectDTO) {
        Result<Boolean> result = remoteService.updateProject(dProjectDTO);
        return result.getData();
    }

    public Boolean deleteProject(BigInteger id) {
        Result<Boolean> result = remoteService.deleteProject(id);
        return result.getData();
    }

    public PageInfo<DProjectOrderVO> findOrders(DProjectOrderDTO dto) {
        Result<PageInfo<DProjectOrderVO>> result = remoteService.findOrders( dto);
        return result.getData();
    }

    public Boolean cancel(BigInteger id) {
        Result<Boolean> result = remoteService.cancelProject(id);
        return result.getData();
    }
}
