package com.mvc.cryptovault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppFinancial;
import com.mvc.cryptovault.common.bean.dto.AppFinancialDTO;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.AppFinancialDetailVO;
import com.mvc.cryptovault.common.bean.vo.AppFinancialOrderVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class AppFinancialService extends BaseService {

    public PageInfo<AppFinancial> getFinancialList(PageDTO pageDTO, String financialName) {
        Result<PageInfo<AppFinancial>> result = remoteService.getFinancialList(pageDTO, financialName);
        return result.getData();
    }

    public AppFinancialDetailVO getFinancialDetail(BigInteger id) {
        Result<AppFinancialDetailVO> result = remoteService.getFinancialDetail(id);
        return result.getData();
    }

    public Boolean saveAppFinancial(AppFinancialDTO appFinancialDTO) {
        Result<Boolean> result = remoteService.saveAppFinancial(appFinancialDTO);
        return result.getData();
    }

    public Boolean updateAppFinancial(AppFinancialDTO appFinancialDTO) {
        Result<Boolean> result = remoteService.updateAppFinancial(appFinancialDTO);
        return result.getData();
    }

    public PageInfo<AppFinancialOrderVO> getFinancialOrderList(BigInteger id, PageDTO pageDTO, String searchKey, Integer status) {
        Result<PageInfo<AppFinancialOrderVO>> result = remoteService.getFinancialOrderList(id, pageDTO, searchKey, status);
        return result.getData();
    }
}
