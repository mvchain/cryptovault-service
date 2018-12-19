package com.mvc.cryptovault.app.job;

import com.mvc.cryptovault.app.service.SmsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author qiyichen
 * @create 2018/12/19 12:43
 */
@Component
public class SmsJob implements CommandLineRunner {
    @Autowired
    SmsService smsService;

    @Override
    @Async
    public void run(String... args) throws Exception {
        while (true) {
            try {
                Thread.sleep(20);
                String cellphone = SmsService.queue.poll();
                if (StringUtils.isNotBlank(cellphone)) {
                    Boolean result = smsService.sendSms(cellphone);
                    if (!result) {
                        SmsService.queue.add(cellphone);
                    }
                }
            } catch (InterruptedException e) {
            }
        }
    }

}
