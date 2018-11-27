package com.mvc.cryptovault.app.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * bean confing
 *
 * @author qiyichen
 * @create 2018/3/12 19:38
 */
@Configuration
public class MyRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Accept-Language", LocaleContextHolder.getLocale().toString());
    }
}
