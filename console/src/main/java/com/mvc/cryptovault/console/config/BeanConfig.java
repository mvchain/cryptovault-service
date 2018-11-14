package com.mvc.cryptovault.console.config;

import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author qiyichen
 * @create 2018/11/13 14:44
 */
@Configuration
public class BeanConfig {

    @Bean
    public HTreeMap hTreeMap() {
        HTreeMap myCache = DBMaker.heapDB().concurrencyScale(16).make().hashMap("consoleCache")
                .expireMaxSize(10000)
                .expireAfterCreate(1, TimeUnit.HOURS)
                .expireAfterUpdate(1, TimeUnit.HOURS)
                .expireAfterGet(1, TimeUnit.HOURS).create();
        return myCache;
    }
}
