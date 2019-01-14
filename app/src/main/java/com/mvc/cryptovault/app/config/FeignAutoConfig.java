package com.mvc.cryptovault.app.config;

import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(Feign.class)
public class FeignAutoConfig {

    @Bean
    public CharlesRequestInterceptor charlesRequestInterceptor(){
        return new CharlesRequestInterceptor();
    }
}