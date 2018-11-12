package com.mvc.cryptovault.app.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;

@FeignClient("console")
public interface AssetRemoteService {
}
