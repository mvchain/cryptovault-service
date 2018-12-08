package com.mvc.cryptovault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.ExportOrders;
import com.mvc.cryptovault.common.bean.dto.AdminTransactionDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTransactionVO;
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
public class TransactionService extends BaseService {


    public PageInfo<DTransactionVO> findTransaction(DTransactionDTO dTransactionDTO) {
        Result<PageInfo<DTransactionVO>> result = remoteService.findTransaction(dTransactionDTO);
        return result.getData();
    }

    public Boolean cancelTransaction(BigInteger id) {
        Result<Boolean> result = remoteService.cancel(id);
        return result.getData();
    }

    public List<ExportOrders> exportCollect() {
        Result<List<ExportOrders>> result = remoteService.exportCollect();
        return result.getData();
    }

    public List<ExportOrders> exportSign() {
        Result<List<ExportOrders>> result = remoteService.exportSign();
        return result.getData();
    }

    public Boolean buy(AdminTransactionDTO dto) {
        Result<Boolean> result = remoteService.buy(dto);
        return result.getData();
    }
}
