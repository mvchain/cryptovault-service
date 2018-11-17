package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AppOrder;
import com.mvc.cryptovault.common.bean.AppOrderDetail;
import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class BlockTransactionService extends AbstractService<BlockTransaction> implements BaseService<BlockTransaction> {

    @Autowired
    AppOrderService orderService;
    @Autowired
    AppOrderDetailService appOrderDetailService;
    @Autowired
    AppUserBalanceService appUserBalanceService;

    public void sendTransaction(BigInteger userId, TransactionDTO transactionDTO) {
        Long id = redisTemplate.boundValueOps(BusinessConstant.APP_PROJECT_ORDER_NUMBER).increment();
        Long now = System.currentTimeMillis();
        BlockTransaction transaction = new BlockTransaction();
        transaction.setCreatedAt(now);
        transaction.setUpdatedAt(now);
        transaction.setOprType(BusinessConstant.OPR_TYPE_WITHDRAW);
        transaction.setValue(transactionDTO.getValue());
        transaction.setUserId(userId);
        transaction.setTokenId(transactionDTO.getTokenId());
        transaction.setStatus(0);
        transaction.setToAddress(transactionDTO.getAddress());
        transaction.setOrderNumber("P" + String.format("%09d", id));
        save(transaction);
        AppOrder appOrder = new AppOrder();
        appOrder.setClassify(0);
        appOrder.setCreatedAt(now);
        appOrder.setUpdatedAt(now);
        appOrder.setFromAddress("");
        appOrder.setHash("");
        appOrder.setOrderContentId(transaction.getId());
        appOrder.setOrderContentName("BLOCK");
        appOrder.setOrderNumber(transaction.getOrderNumber());
        appOrder.setValue(transactionDTO.getValue());
        appOrder.setUserId(userId);
        appOrder.setTokenId(transactionDTO.getTokenId());
        appOrder.setStatus(0);
        appOrder.setOrderType(1);
        orderService.save(appOrder);
        AppOrderDetail detail = new AppOrderDetail();
        detail.setCreatedAt(now);
        detail.setUpdatedAt(now);
        detail.setFee(BigDecimal.ZERO);
        detail.setFromAddress("");
        detail.setHash("");
        detail.setToAddress(transactionDTO.getAddress());
        detail.setOrderId(appOrder.getId());
        detail.setValue(transactionDTO.getValue());
        appOrderDetailService.save(detail);
        appUserBalanceService.updateBalance(userId, transaction.getTokenId(), BigDecimal.ZERO.subtract(transaction.getValue()));
    }
}