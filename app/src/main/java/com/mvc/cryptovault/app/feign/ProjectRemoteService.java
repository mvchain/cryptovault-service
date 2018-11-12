package com.mvc.cryptovault.app.feign;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient("console")
public interface ProjectRemoteService {
}
