package com.mvc.cryptovault.console.dashboard.controller;

import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
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

    @GetMapping("count")
    public Result<Integer> accountCount(@RequestParam(value = "tokenType", required = false) String tokenType) {
        Integer count = blockHeightService.accountCount(tokenType);
        return new Result<>(count);
    }

    @PostMapping("")
    public Result<Boolean> importAddress(@RequestBody List<CommonAddress> list) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        blockHeightService.importAddress(list);
        return new Result<>();
    }

}
