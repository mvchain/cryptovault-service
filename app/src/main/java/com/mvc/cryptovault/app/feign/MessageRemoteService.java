package com.mvc.cryptovault.app.feign;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppMessage;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

@FeignClient("console")
public interface MessageRemoteService {

    @GetMapping("appMessage")
    Result<PageInfo<AppMessage>> getlist(@RequestParam("userId") BigInteger userId, @RequestParam("timestamp") BigInteger timestamp, @RequestParam("type") Integer type, @RequestParam("pageSize") Integer pageSize);

    @PutMapping("appMessage/{id}")
    Result<Boolean> read(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id);
}
