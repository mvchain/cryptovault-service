package com.mvc.cryptovault.app.job;

import com.mvc.cryptovault.app.service.MailService;
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
    MailService mailService;

    @Override
    @Async
    public void run(String... args) throws Exception {
        while (true) {
            try {
                Thread.sleep(20);
                String cellphone = MailService.queue.poll();
                if (StringUtils.isNotBlank(cellphone)) {
                    mailService.sendSms(cellphone);
                }
            } catch (InterruptedException e) {
            }
        }
    }

}
