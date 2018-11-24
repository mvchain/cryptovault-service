package com.mvc.cryptovault.dashboard.config;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.vo.Result;
import feign.Response;
import feign.Util;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static feign.FeignException.errorStatus;

@Configuration
public class MyErrorDecoder implements feign.codec.ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            //处理service抛出的异常
            if (response.status() == 400) {
                String obj = null;
                try {
                    obj = Util.toString(response.body().asReader());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Result result = JSON.parseObject(obj, Result.class);
                return new IllegalArgumentException(result.getMessage());
            }
            return errorStatus(methodKey, response);
        }
    }