package com.mvc.cryptovault.app.feign;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

@FeignClient("console")
public interface TokenRemoteService {
    @GetMapping("commonToken")
    Result<PageInfo<CommonToken>> all(@RequestParam("visiable") Integer visiable, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("updatedStart") BigInteger timestamp);

    @GetMapping("commonTokenPrice")
    Result<PageInfo<CommonTokenPrice>> price();

}