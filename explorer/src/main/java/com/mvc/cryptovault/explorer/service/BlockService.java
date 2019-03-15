package com.mvc.cryptovault.explorer.service;

import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.explorer.bean.PageDTO;
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
public class BlockService extends BaseService {

    public NowBlockVO getLast() {
        Result<NowBlockVO> result = remoteService.getLast();
        return result.getData();
    }

    public List<ExplorerTransactionSimpleVO> getLastTransaction(PageDTO pageDTO) {
        Result<List<ExplorerTransactionSimpleVO>> result = remoteService.getLastTransaction(pageDTO.getPageSize());
        return result.getData();
    }

    public List<ExplorerSimpleVO> getBlocks(BigInteger blockId, PageDTO pageDTO) {
        Result<List<ExplorerSimpleVO>> result = remoteService.getBlocks(blockId, pageDTO.getPageSize());
        return result.getData();
    }

    public ExplorerSimpleVO getBlockInfo(BigInteger blockId) {
        Result<ExplorerSimpleVO> result = remoteService.getBlockInfo(blockId);
        return result.getData();
    }

    public List<ExplorerTransactionSimpleVO> getBlockTransaction(BigInteger blockId, BigInteger transactionId, PageDTO pageDTO) {
        Result<List<ExplorerTransactionSimpleVO>> result = remoteService.getBlockTransaction(blockId, transactionId, pageDTO.getPageSize());
        return result.getData();
    }

    public ExplorerTransactionDetailVO getTxDetail(String hash) {
        Result<ExplorerTransactionDetailVO> result = remoteService.getTxDetail(hash);
        return result.getData();
    }

    public String publicKeyExist(String publicKey) {
        Result<String> result = remoteService.publicKeyExist(publicKey);
        return result.getData();
    }

    public List<ExplorerBalanceVO> getBalance(String publicKey) {
        Result<List<ExplorerBalanceVO>> result = remoteService.getBalance(publicKey);
        return result == null ? null : result.getData();
    }

    public List<ExplorerSimpleOrder> getOrders(String publicKey, PageDTO pageDTO, BigInteger id) {
        Result<List<ExplorerSimpleOrder>> result = remoteService.getOrders(publicKey, pageDTO.getPageSize(), id);
        return result.getData();
    }

    public ExplorerOrder getOrderDetail(BigInteger id) {
        Result<ExplorerOrder> result = remoteService.getOrderDetail(id);
        return result.getData();
    }

}
