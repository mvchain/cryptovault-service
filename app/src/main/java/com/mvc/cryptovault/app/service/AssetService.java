package com.mvc.cryptovault.app.service;

import com.mvc.cryptovault.app.feign.ConsoleRemoteService;
import com.mvc.cryptovault.common.bean.dto.DebitDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionSearchDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Service
public class AssetService {

    @Autowired
    ConsoleRemoteService consoleRemoteService;

    public List<TokenBalanceVO> getAsset(BigInteger userId) {
        Result<List<TokenBalanceVO>> result = consoleRemoteService.getAsset(userId);
        return result.getData();
    }

    public BigDecimal getBalance(BigInteger userId) {
        Result<BigDecimal> result = consoleRemoteService.getBalance(userId);
        return result.getData();
    }

    public List<TransactionSimpleVO> getTransactions(BigInteger userId, TransactionSearchDTO transactionSearchDTO) {
        Result<List<TransactionSimpleVO>> result = consoleRemoteService.getTransactions(userId, transactionSearchDTO.getTransactionType(), transactionSearchDTO.getId(), transactionSearchDTO.getType(), transactionSearchDTO.getPageSize());
        return result.getData();
    }

    public TransactionDetailVO getTransaction(BigInteger userId, BigInteger id) {
        Result<TransactionDetailVO> result = consoleRemoteService.getTransaction(userId, id);
        return result.getData();
    }

    public String getAddress(BigInteger userId, BigInteger tokenId) {
        Result<String> result = consoleRemoteService.getAddress(userId, tokenId);
        return result.getData();
    }

    public BigDecimal debit(BigInteger userId) {
        Result<BigDecimal> result = consoleRemoteService.debit(userId);
        return result.getData();
    }

    public Boolean debit(BigInteger userId, DebitDTO debitDTO) {
        Result<Boolean> result = consoleRemoteService.debit(userId, debitDTO);
        return result.getData();
    }

    public TransactionTokenVO getTransactionInfo(BigInteger userId, BigInteger tokenId) {
        Result<TransactionTokenVO> result = consoleRemoteService.getTransactionInfo(userId, tokenId);
        return result.getData();
    }

    public Boolean sendTransaction(BigInteger userId, TransactionDTO transactionDTO) {
        Result<Boolean> result = consoleRemoteService.sendTransaction(userId, transactionDTO);
        return result.getData();
    }
}
