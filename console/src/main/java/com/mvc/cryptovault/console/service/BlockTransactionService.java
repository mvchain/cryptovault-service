package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class BlockTransactionService extends AbstractService<BlockTransaction> implements BaseService<BlockTransaction> {

    public void sendTransaction(BigInteger userId, TransactionDTO transactionDTO) {
        BlockTransaction transaction = new BlockTransaction();
        transaction.setCreatedAt(System.currentTimeMillis());
        transaction.setUpdatedAt(System.currentTimeMillis());
        transaction.setOprType(BusinessConstant.OPR_TYPE_WITHDRAW);
        transaction.setValue(transactionDTO.getValue());
        transaction.setUserId(userId);
        transaction.setTokenId(transaction.getTokenId());
        transaction.setStatus(0);
        transaction.setToAddress(transactionDTO.getAddress());
        save(transaction);
        //TODO 添加到统一列表
    }
}