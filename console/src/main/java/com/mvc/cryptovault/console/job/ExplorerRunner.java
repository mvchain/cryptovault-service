package com.mvc.cryptovault.console.job;

import com.mvc.cryptovault.common.bean.ExplorerBlockInfo;
import com.mvc.cryptovault.common.bean.ExplorerBlockSetting;
import com.mvc.cryptovault.common.util.MnemonicUtil;
import com.mvc.cryptovault.common.util.bip39.Words;
import com.mvc.cryptovault.console.service.ExplorerBlockInfoService;
import com.mvc.cryptovault.console.service.ExplorerBlockSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author qiyichen
 * @create 2018/12/10 16:55
 */
@Component
public class ExplorerRunner implements CommandLineRunner {

    @Autowired
    ExplorerBlockInfoService explorerBlockInfoService;
    @Autowired
    ExplorerBlockSettingService explorerBlockSettingService;

    @Override
    @Async
    public void run(String... args) throws Exception {
        try {
            while (true) {
                addExplorerBlockInfo();
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

    private void addExplorerBlockInfo() {
        ExplorerBlockSetting setting = explorerBlockSettingService.findById(BigInteger.ONE);
        if (null == setting) {
            sleep(1000L);
            return;
        }
        Long nowNumber = null;
        ExplorerBlockInfo explorerBlockInfo = explorerBlockInfoService.getLast();
        if (null == explorerBlockInfo) {
            nowNumber = 1L;
        } else {
            nowNumber = explorerBlockInfo.getId().longValue() + 1L;
        }
        if (System.currentTimeMillis() / 5000L < (nowNumber + 309254400)) {
            sleep(5000L);
        }
        try {
            String hash = MnemonicUtil.getRandomCode(Words.TWENTY_ONE);
            ExplorerBlockInfo newInfo = new ExplorerBlockInfo();
            newInfo.setCreatedAt((nowNumber - 1 + 309254400) * 5000L);
            newInfo.setDifficult(setting.getRandomDifficult());
            newInfo.setHash(hash);
            newInfo.setId(BigInteger.valueOf(nowNumber));
            newInfo.setTransactions(setting.getRandomTransactionCount());
            explorerBlockInfoService.saveInfo(newInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = dateformat.parse("2019-01-01 00:00:00").getTime();
        System.out.println(time / 5000);
    }

}
