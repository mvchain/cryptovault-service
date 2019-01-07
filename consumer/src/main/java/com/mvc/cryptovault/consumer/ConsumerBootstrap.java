package com.mvc.cryptovault.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author qiyichen
 * @create 2018/12/25 14:51
 */
@SpringBootApplication
@Configuration
@MapperScan(basePackages = "com.mvc.cryptovault.consumer.mapper")
public class ConsumerBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerBootstrap.class, args);
    }
}
