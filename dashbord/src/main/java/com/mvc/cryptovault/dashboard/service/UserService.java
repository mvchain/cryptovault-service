package com.mvc.cryptovault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUSerDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserBalanceVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserLogVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserService extends BaseService {


    public PageInfo<DUSerVO> findUser(PageDTO pageDTO, String cellphone) {
        Result<PageInfo<DUSerVO>> result = remoteService.findUser(pageDTO, cellphone);
        return result.getData();
    }

    public DUSerDetailVO getUserDetail(BigInteger id) {
        Result<DUSerDetailVO> result = remoteService.getUserDetail(id);
        return result.getData();
    }

    public List<DUserBalanceVO> getBalance(BigInteger id) {
        Result<List<DUserBalanceVO>> result = remoteService.getUserBalance(id);
        return result.getData();
    }

    public PageInfo<DUserLogVO> getUserLog(BigInteger id, PageDTO pageDTO) {
        Result<PageInfo<DUserLogVO>> result = remoteService.getUserLog(pageDTO, id);
        return result.getData();
    }

    public Boolean updateStatus(BigInteger id, Integer status) {
        remoteService.updateUserStatus(id, status);
        return true;
    }
}
