package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/14 14:40
 */
@RestController
@RequestMapping("appUserAddress")
public class AppUserAddressController extends BaseController {

    @GetMapping("{userId}")
    public Result<String> getAddress(@PathVariable("userId") BigInteger userId, @RequestParam("tokenId") BigInteger tokenId) {
        String address = appUserAddressService.getAddress(userId, tokenId);
        return new Result<>(address);
    }

}
