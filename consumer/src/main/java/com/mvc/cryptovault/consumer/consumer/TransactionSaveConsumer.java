package com.mvc.cryptovault.consumer.consumer;

/**
 * @author qiyichen
 * @create 2018/12/25 17:14
 */

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.AppUserTransaction;
import com.mvc.cryptovault.common.bean.TokenVolume;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.constant.BusinessConstant;
import com.mvc.cryptovault.common.constant.RocketMqConstant;
import com.mvc.cryptovault.consumer.mapper.AppUserTransactionMapper;
import com.mvc.cryptovault.consumer.service.AppOrderService;
import com.mvc.cryptovault.consumer.util.BeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.starter.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.starter.core.RocketMQListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
//@Service
@Component
@RocketMQMessageListener(topic = "TopicTest232", consumerGroup = "cryptovault-service")
public class TransactionSaveConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String dto) {
        System.out.println(dto);
    }
//
//    @Autowired
//    private AppOrderService appOrderService;
//    @Autowired
//    AppUserTransactionMapper appUserTransactionMapper;
//
//    @Override
//    public void onMessage(TransactionBuyDTO dto) {
//        Long time = System.currentTimeMillis();
//        AppUserTransaction transaction = BeanBuilder.buildAppUserTransaction(dto);
//        //是否为买卖订单
//        Boolean merchandise = null != dto.getId() && !dto.getId().equals(BigInteger.ZERO);
//        if (merchandise) {
//            //买卖订单
//
//        } else {
//            //挂单交易
//            saveOrder(transaction);
//        }
//
//        updateBalance(userId, dto, pair);
//        if () {
//            //普通挂单
//            saveOrder(transaction);
//        } else {
//            saveChildTransaction(userId, dto, pair, time, transaction, targetTransaction);
//            //记录成交量
//            TokenVolume tokenVolume = new TokenVolume();
//            tokenVolume.setCreatedAt(System.currentTimeMillis());
//            tokenVolume.setValue(dto.getValue());
//            tokenVolume.setTokenId(pair.getTokenId());
//            tokenVolume.setUsed(0);
//            if (pair.getTokenId().compareTo(BusinessConstant.BASE_TOKEN_ID_USDT) <= 0) {
//                tokenVolume.setUsed(1);
//            }
//            tokenVolumeService.save(tokenVolume);
//        }
//
//        System.out.println(JSON.toJSON(dto));
//        log.info("------- MessageExtConsumer received message, msgId:{}, body:{} ", message.getMsgId(), new String(message.getBody()));
//    }
//
//
//    private void saveOrder(AppUserTransaction transaction) {
//        transaction.setStatus(BusinessConstant.TRANSACTION_STATUS_CANCEL);
//        transaction.setParentId(BigInteger.ZERO);
//        transaction.setSuccessValue(BigDecimal.ZERO);
//        transaction.setTargetUserId(BigInteger.ZERO);
//        transaction.setSelfOrder(BusinessConstant.TRANSACTION_SELF);
//        appUserTransactionMapper.insert(transaction);
//    }
//
//    private void saveBuy(AppUserTransaction transaction, TransactionBuyDTO dto) {
//        AppUserTransaction targetTransaction = appUserTransactionMapper.selectByPrimaryKey(dto.getId());
//        Integer result = appUserTransactionMapper.updateValue(dto.getId(), dto.getValue(), System.currentTimeMillis());
//        if(result  == 0)return;
//        //生成用户主动交易记录
//        transaction.setStatus(BusinessConstant.TRANSACTION_STATUS_COMPLETE);
//        transaction.setParentId(targetTransaction.getId());
//        transaction.setSuccessValue(dto.getValue());
//        transaction.setTargetUserId(targetTransaction.getUserId());
//        transaction.setSelfOrder(BusinessConstant.TRANSACTION_SELF);
//        appUserTransactionMapper.insert(transaction);
//        appOrderService.saveOrder(transaction, pair);
//        //生成目标用户交易记录
//        AppUserTransaction targetSubTransaction = new AppUserTransaction();
//        BeanUtils.copyProperties(transaction, targetSubTransaction);
//        targetSubTransaction.setId(null);
//        targetSubTransaction.setUserId(targetTransaction.getUserId());
//        targetSubTransaction.setTargetUserId(dto.getUserId());
//        targetSubTransaction.setParentId(targetTransaction.getId());
//        targetSubTransaction.setOrderNumber(BeanBuilder.getOrderNumber());
//        targetSubTransaction.setTransactionType(dto.getTransactionType().equals(1) ? 2 : 1);
//        targetSubTransaction.setSelfOrder(0);
//        appUserTransactionMapper.insert(targetSubTransaction);
//        appOrderService.saveOrder(targetSubTransaction, pair);
//        //更新余额
//        if (dto.getTransactionType().equals(BusinessConstant.TRANSACTION_TYPE_BUY)) {
//
//
//        } else {
//
//        }
//        //添加到成交量队列
//

//    }

}
