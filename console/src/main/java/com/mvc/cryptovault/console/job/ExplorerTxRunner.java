package com.mvc.cryptovault.console.job;

import com.mvc.cryptovault.common.bean.ExplorerBlockTransaction;
import com.mvc.cryptovault.common.bean.ExplorerBlockUser;
import com.mvc.cryptovault.common.util.MnemonicUtil;
import com.mvc.cryptovault.common.util.bip39.Words;
import com.mvc.cryptovault.console.service.ExplorerBlockInfoService;
import com.mvc.cryptovault.console.service.ExplorerBlockSettingService;
import com.mvc.cryptovault.console.service.ExplorerBlockTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * @author qiyichen
 * @create 2018/12/10 16:55
 */
@Component
public class ExplorerTxRunner implements CommandLineRunner {

    @Autowired
    ExplorerBlockInfoService explorerBlockInfoService;
    @Autowired
    ExplorerBlockSettingService explorerBlockSettingService;
    @Autowired
    ExplorerBlockTransactionService explorerBlockTransactionService;

    @Override
    @Async
    public void run(String... args) throws Exception {
        try {
            while (true) {
                Long nowNumber = null;
                ExplorerBlockTransaction explorerBlockInfo = explorerBlockTransactionService.getLast();
                if (null == explorerBlockInfo) {
                    nowNumber = 1L;
                } else {
                    nowNumber = explorerBlockInfo.getId().longValue() + 1L;
                }
                if (nowNumber >= ExplorerBlockTransaction.TX_MAX_NUMBER) {
                    break;
                }
                addExplorerBlockTx(nowNumber);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sleep(Long time) {
        try {
            Thread.sleep(null == time ? 1000L : time);
        } catch (Exception e) {
        }
    }

    private void addExplorerBlockTx(Long nowNumber) {
        try {
            ExplorerBlockTransaction tx = new ExplorerBlockTransaction();
            BigInteger from = BigInteger.valueOf(1 + (int) (Math.random() * ExplorerBlockUser.MAX_VALUE));
            BigInteger to = BigInteger.valueOf(1 + (int) (Math.random() * ExplorerBlockUser.MAX_VALUE));
            to = to.equals(from) ? to.equals(ExplorerBlockUser.MAX_VALUE) ? to.subtract(BigInteger.ONE) : to.add(BigInteger.ONE) : to;
            tx.setCreatedAt((long) Math.random() * 5000);
            tx.setFromUserId(from);
            tx.setHash(MnemonicUtil.getRandomCode(Words.TWELVE));
            tx.setId(BigInteger.valueOf(nowNumber));
            tx.setTokenId(BigInteger.TWO);
            tx.setTokenName("MVC");
            tx.setToUserId(to);
            tx.setValue(new BigDecimal(Math.random() * 50).setScale(2, RoundingMode.DOWN));
            explorerBlockTransactionService.saveInfo(tx);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
