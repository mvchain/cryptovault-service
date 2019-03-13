package com.mvc.cryptovault.console.job;

import com.mvc.cryptovault.console.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author qiyichen
 * @create 2018/12/10 16:55
 */
@Component
public class PublicKeyRunner implements CommandLineRunner {

    @Autowired
    AppUserService appUserService;

    @Override
    @Async
    public void run(String... args) throws Exception {
        appUserService.updatePublicKey();
    }

}
