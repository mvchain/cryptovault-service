package com.mvc.cryptovault.console.dashboard.controller;

import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiyichen
 * @create 2018/11/21 16:42
 */
@RestController
@RequestMapping("dashboard/commonAddress")
public class DCommonAddressController extends BaseController {

    @GetMapping("count")
    public Result<Integer> accountCount(@RequestParam(value = "tokenType", required = false) String tokenType) {
        //TODO 区块链相关后续处理
        return new Result<>(100);
    }

}
