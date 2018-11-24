package com.mvc.cryptovault.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qiyichen
 * @create 2018/11/5 16:10
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableSwagger2
@EnableAsync
@Configuration
@ComponentScan({"com.mvc.cryptovault.common.swaggermock", "com.mvc.cryptovault"})
public class TestBoardBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(TestBoardBootstrap.class, args);
    }

}
