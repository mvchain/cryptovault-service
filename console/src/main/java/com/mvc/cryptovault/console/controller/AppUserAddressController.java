package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppUserAddressService;
import com.mvc.cryptovault.console.service.BlockTransactionService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/14 14:40
 */
@RestController
@RequestMapping("appUserAddress")
public class AppUserAddressController extends BaseController {

    @Autowired
    AppUserAddressService appUserAddressService;
    @Autowired
    BlockTransactionService blockTransactionService;

    @GetMapping("{userId}")
    public Result<String> getAddress(@PathVariable("userId") BigInteger userId, @RequestParam("tokenId") BigInteger tokenId) {
        String address = appUserAddressService.getAddress(userId, tokenId);
        return new Result<>(address);
    }

    @GetMapping("address")
    public Result<Boolean> isInner(@RequestParam String address) {
        boolean result = appUserAddressService.isInner(address);
        return new Result<>(result);
    }
}
