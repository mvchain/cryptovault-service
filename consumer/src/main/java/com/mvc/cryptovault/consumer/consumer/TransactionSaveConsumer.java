package com.mvc.cryptovault.consumer.consumer;

/**
 * @author qiyichen
 * @create 2018/12/25 17:14
 */

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.AppUserTransaction;
import com.mvc.cryptovault.common.bean.TokenVolume;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.constant.RocketMqConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.starter.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.starter.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Slf4j
@Service
@RocketMQMessageListener(topic = RocketMqConstant.APP_UER_TRANSACTION, consumerGroup = "cryptovault-service")
public class TransactionSaveConsumer implements RocketMQListener<TransactionBuyDTO> {

    @Override
    public void onMessage(TransactionBuyDTO dto) {
//        Long time = System.currentTimeMillis();
//        AppUserTransaction transaction = getAppUserTransaction(userId, dto, time);
//        updateBalance(userId, dto, pair);
//        if (null == dto.getId() || dto.getId().equals(BigInteger.ZERO)) {
//            //普通挂单
//            saveTopTransaction(transaction);
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
    }

}
