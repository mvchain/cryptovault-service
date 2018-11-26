package com.mvc.cryptovault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppProjectUserTransaction;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectOrderDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectOrderVO;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/21 16:44
 */
@RestController
@RequestMapping("dashboard/appProjectUserTransaction")
public class DAppProjectUserTransactionController extends BaseController {

    @PutMapping("{id}")
    public Result<Boolean> cancelProject(@PathVariable("id") BigInteger id) {
        AppProjectUserTransaction appProjectUserTransaction = appProjectUserTransactionService.findById(id);
        Assert.isTrue(appProjectUserTransaction.getResult() == 0, "无法取消");
        appProjectUserTransaction.setResult(4);
        appProjectUserTransactionService.update(appProjectUserTransaction);
        appProjectUserTransactionService.updateAllCache();
        appProjectUserTransactionService.updateCache(id);
        return new Result<>(true);
    }

    @GetMapping("")
    public Result<PageInfo<DProjectOrderVO>> findOrders(@ModelAttribute PageDTO pageDTO, @ModelAttribute DProjectOrderDTO dto) {
        PageInfo<DProjectOrderVO> result = appProjectUserTransactionService.findOrders(pageDTO, dto);
        return new Result<>(result);
    }

}
