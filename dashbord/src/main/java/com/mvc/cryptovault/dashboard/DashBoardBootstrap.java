package com.mvc.cryptovault.dashboard;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import io.swagger.annotations.SwaggerDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author qiyichen
 * @create 2018/11/5 16:10
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAsync
@Configuration
@EnableCircuitBreaker
@EnableHystrix
@EnableEurekaClient
@EnableFeignClients
@EnableWebMvc
@EnableDiscoveryClient
public class DashBoardBootstrap extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DashBoardBootstrap.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(DashBoardBootstrap.class, args);
    }

}
