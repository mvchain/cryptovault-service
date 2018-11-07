package com.mvc.cryptovault.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qiyichen
 * @create 2018/11/5 16:10
 */
@SpringBootApplication
@EnableSwagger2
@EnableAsync
@Configuration
@ComponentScan({"com.mvc.cryptovault.common.swaggermock", "com.mvc.cryptovault"})
@EnableCircuitBreaker
@EnableHystrix
public class AppBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(AppBootstrap.class, args);
    }

}
