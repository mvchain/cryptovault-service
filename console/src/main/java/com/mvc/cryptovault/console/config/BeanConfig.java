package com.mvc.cryptovault.console.config;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import com.mvc.cryptovault.common.util.JwtHelper;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author qiyichen
 * @create 2018/11/13 14:44
 */
@Configuration
public class BeanConfig {

    @Value("${jpush.secret}")
    private String MASTER_SECRET;

    @Value("${jpush.app_key}")
    private String APP_KEY;

    @Value("${service.name}")
    private String serviceName;
    @Value("${service.expire}")
    private Long expire;
    @Value("${service.refresh}")
    private Long refresh;
    @Value("${service.base64Secret}")
    private String base64Secret;

    @Bean
    public HTreeMap hTreeMap() {
        HTreeMap myCache = DBMaker.heapDB().concurrencyScale(16).make().hashMap("consoleCache")
                .expireMaxSize(10000)
                .expireAfterCreate(1, TimeUnit.HOURS)
                .expireAfterUpdate(1, TimeUnit.HOURS)
                .expireAfterGet(1, TimeUnit.HOURS).create();
        return myCache;
    }

    @Bean
    JwtHelper jwtHelper() {
        JwtHelper.serviceName = serviceName;
        JwtHelper.expire = expire;
        JwtHelper.refresh = refresh;
        JwtHelper.base64Secret = base64Secret;
        return new JwtHelper();
    }

    @Bean
    public JPushClient jPushClient() {
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
        return jpushClient;
    }
}
