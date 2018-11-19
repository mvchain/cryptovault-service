package com.mvc.cryptovault.app.config;

import feign.Contract;
import feign.Feign;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;

import java.util.Collections;

@Configuration
@ConditionalOnClass(Feign.class)
public class FeignAutoConfig {

    @Bean
    public CharlesRequestInterceptor charlesRequestInterceptor(){
        return new CharlesRequestInterceptor();
    }
}