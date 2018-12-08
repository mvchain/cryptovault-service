package com.mvc.cryptovault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.BlockSign;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.vo.AdminWalletVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockStatusDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockeTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DBlockeTransactionVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DHoldVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class BlockService extends BaseService {

    public List<DHoldVO> getHold() {

        Result<List<DHoldVO>> result = remoteService.getHold();
        return result.getData();
    }

    public Boolean setHold(List<DHoldVO> list) {
        Result<Boolean> result = remoteService.setHold(list);
        return result.getData();
    }

    public List<DHoldVO> getFee() {
        Result<List<DHoldVO>> result = remoteService.getFee();
        return result.getData();
    }

    public Boolean setFee(List<DHoldVO> list) {
        Result<Boolean> result = remoteService.setFee(list);
        return result.getData();
    }

    public PageInfo<DBlockeTransactionVO> getTransactions(DBlockeTransactionDTO dBlockeTransactionDTO) {
        Result<PageInfo<DBlockeTransactionVO>> result = remoteService.getTransactions(dBlockeTransactionDTO);
        return result.getData();
    }

    public BigDecimal getBalance(BigInteger tokenId) {
        Result<BigDecimal> result = remoteService.getBalance(tokenId);
        return result.getData();
    }

    public Integer accountCount(String tokenType) {
        Result<Integer> result = remoteService.accountCount(tokenType);
        return result.getData();
    }

    public Boolean updateStatus(DBlockStatusDTO dBlockStatusDTO) {
        Result<Boolean> result = remoteService.updateStatus(dBlockStatusDTO);
        return result.getData();
    }

    public Boolean importAddress(List<CommonAddress> list, String fileName) {
        remoteService.importAddress(list, fileName);
        return true;
    }

    public Boolean importSign(List<BlockSign> list, String fileName) {
        remoteService.importSign(list, fileName);
        return true;
    }

    public AdminWalletVO getAdminWallet() {
        Result<AdminWalletVO> result = remoteService.getAdminWallet();
        return result.getData();
    }
}
