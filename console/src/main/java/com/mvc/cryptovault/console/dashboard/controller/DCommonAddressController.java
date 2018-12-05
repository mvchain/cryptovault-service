package com.mvc.cryptovault.console.dashboard.controller;

import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.BlockHeightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/21 16:42
 */
@RestController
@RequestMapping("dashboard/commonAddress")
public class DCommonAddressController extends BaseController {
    @Autowired
    BlockHeightService blockHeightService;

    @GetMapping("count")
    public Result<Integer> accountCount(@RequestParam(value = "tokenType", required = false) String tokenType) {
        Integer count = blockHeightService.accountCount(tokenType);
        return new Result<>(count);
    }

    @PostMapping("")
    public Result<Boolean> importAddress(@RequestBody List<CommonAddress> list, @RequestParam String fileName) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        String key = RedisConstant.ADDRESS_IMPORT + fileName;
        String obj = redisTemplate.boundValueOps(key).get();
        if (null != obj) {
            throw new IllegalArgumentException("该文件正在导入,请稍后");
        }
        blockHeightService.importAddress(list, fileName);
        return new Result<>();
    }

}
