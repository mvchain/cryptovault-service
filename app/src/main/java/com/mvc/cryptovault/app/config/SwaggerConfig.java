package com.mvc.cryptovault.app.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Configuration
public class SwaggerConfig extends WebMvcConfigurationSupport {

    /**
     * 发现如果继承了WebMvcConfigurationSupport，则在yml中配置的相关内容会失效。
     * 需要重新指定静态资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(0);
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
        InterceptorRegistration addInterceptor = registry.addInterceptor(new ServiceAuthRestInterceptor());
        // 排除配置
        addInterceptor.excludePathPatterns("/error");
        addInterceptor.excludePathPatterns("/login**");
        addInterceptor.excludePathPatterns("/webjars/**");
        addInterceptor.excludePathPatterns("/static/");
        addInterceptor.excludePathPatterns("/");
        addInterceptor.excludePathPatterns("/csrf");
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
        String[] urls = {
                "/v2/api-docs",
                "/swagger-resources/**",
                "/cache/**",
                "/api/log/save",
                "/swagger-ui.html"
        };
        addInterceptor.excludePathPatterns(urls);
    }

    @Bean
    public Docket createRestApi() {
        //可以添加多个header或参数
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        ParameterBuilder aParameterBuilder2 = new ParameterBuilder();
        aParameterBuilder
                .parameterType("header")
                .name("Authorization")
                .description("token")
                .modelRef(new ModelRef("string"))
                .required(false).build();
        aParameterBuilder2
                .parameterType("header")
                .name("Accept-Language")
                .description("国际化设置,目前只支持zh-cn/en-US,如需扩展参考官方字段说明")
                .modelRef(new ModelRef("string"))
                .required(false).build();
        List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(aParameterBuilder.build());
        aParameters.add(aParameterBuilder2.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.mvc.cryptovault.app"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(aParameters);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("app接口API")
                .description("myapp")
                .version("1.0")
                .build();
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
        // 默认语言
        slr.setDefaultLocale(Locale.US);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 参数名
        return lci;
    }


    @Override
    @Bean
    public Validator getValidator() {
        ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
        rbms.setDefaultEncoding("UTF-8");
        rbms.setBasenames("messages");
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(rbms);
        return validator;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.PrettyFormat
        );
        converters.add(converter);
    }
}