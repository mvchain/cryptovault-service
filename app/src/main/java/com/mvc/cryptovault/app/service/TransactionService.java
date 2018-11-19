package com.mvc.cryptovault.app.service;

import com.mvc.cryptovault.app.feign.ConsoleRemoteService;
import com.mvc.cryptovault.common.bean.dto.MyTransactionDTO;
import com.mvc.cryptovault.common.bean.dto.OrderDTO;
import com.mvc.cryptovault.common.bean.dto.PairDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    ConsoleRemoteService transactionRemoteService;

    public List<PairVO> getPair(BigInteger userId, PairDTO pairDTO) {
        Result<List<PairVO>> result = transactionRemoteService.getPair(pairDTO);
        return result.getData();
    }

    public List<OrderVO> getTransactions(BigInteger userId, OrderDTO dto) {
        Result<List<OrderVO>> result = transactionRemoteService.getTransactions(dto);
        return result.getData();
    }

    public KLineVO getKLine(BigInteger userId, BigInteger pairId) {
        Result<KLineVO> result = transactionRemoteService.getTransactions(pairId);
        return result.getData();
    }

    public List<MyOrderVO> getUserTransactions(BigInteger userId, MyTransactionDTO dto) {
        Result<List<MyOrderVO>> result = transactionRemoteService.getUserTransactions(userId, dto);
        return result.getData();
    }

    public Boolean buy(BigInteger userId, TransactionBuyDTO dto) {
        Result<Boolean> result = transactionRemoteService.buy(userId, dto);
        return result.getData();
    }

    public OrderInfoVO getInfo(BigInteger userId, BigInteger pairId, Integer transactionType) {
        Result<OrderInfoVO> result = transactionRemoteService.getInfo(userId, pairId, transactionType);
        return result.getData();
    }

    public Boolean cancel(BigInteger userId, BigInteger id) {
        Result<Boolean> result = transactionRemoteService.cancel(userId, id);
        return result.getData();
    }
}
