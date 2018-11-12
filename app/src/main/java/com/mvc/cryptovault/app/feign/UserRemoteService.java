package com.mvc.cryptovault.app.feign;

import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

@FeignClient("console")
public interface UserRemoteService {

    @GetMapping("user/{id}")
    Result<AppUser> getUserById(@PathVariable("id") BigInteger userId);

    @GetMapping("user/username")
    Result<AppUser> getUserByUsername(@RequestParam("username") String username);

}
