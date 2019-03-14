package com.mvc.cryptovault.console.job;

import com.mvc.cryptovault.common.bean.ExplorerBlockUser;
import com.mvc.cryptovault.common.util.MnemonicUtil;
import com.mvc.cryptovault.common.util.bip39.Words;
import com.mvc.cryptovault.console.service.ExplorerBlockUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/10 16:55
 */
@Component
public class ExplorerUserRunner implements CommandLineRunner {

    @Autowired
    ExplorerBlockUserService userService;

    @Override
    @Async
    public void run(String... args) throws Exception {
        try {
            while (true) {
                Long nowNumber = null;
                ExplorerBlockUser explorerBlockInfo = userService.getLast();
                if (null == explorerBlockInfo) {
                    nowNumber = 1L;
                } else {
                    nowNumber = explorerBlockInfo.getId().longValue() + 1L;
                }
                if (nowNumber >= ExplorerBlockUser.MAX_VALUE) {
                    break;
                }
                addExplorerBlockUser(nowNumber);
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

    private void addExplorerBlockUser(Long nowNumber) {

        try {
            ExplorerBlockUser user = new ExplorerBlockUser();
            user.setId(BigInteger.valueOf(nowNumber));
            user.setCreatedAt(System.currentTimeMillis());
            user.setPublicKey(MnemonicUtil.getRandomCode(Words.TWELVE));
            userService.saveInfo(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
