package com.mvc.cryptovault.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author qiyichen
 * @create 2018/11/5 16:10
 */
@SpringBootApplication
@EnableSwagger2
@EnableAsync
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = "com.mvc.cryptovault.console.dao")
@ComponentScan({"com.mvc.cryptovault.common.swaggermock", "com.mvc.cryptovault"})
@EnableCircuitBreaker
@EnableEurekaClient
@EnableDiscoveryClient
@EnableHystrix
public class ConsoleBootstrap {

    public static void main(String[] args) {

        SpringApplication.run(ConsoleBootstrap.class, args);
    }

}
