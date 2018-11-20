package com.mvc.cryptovault.dashboard.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("console")
public interface ConsoleRemoteService {
}
