package com.mvc.cryptovault.app.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Component
@Configuration("admimWebConfig")
public class SpringContextUtil extends WebMvcConfigurationSupport implements BeanFactoryAware {
    private static BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        SpringContextUtil.beanFactory = beanFactory;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(new ServiceAuthRestInterceptor());
        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/login**");
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
        String[] urls = {
                "/v2/api-docs",
                "/swagger-resources/**",
                "/cache/**",
                "/api/log/save"
        };
        addInterceptor.excludePathPatterns(urls);
    }

    public static <T> T getBean(String beanName) {
        if (null != beanFactory) {
            return (T) beanFactory.getBean(beanName);
        }
        return null;
    }

}