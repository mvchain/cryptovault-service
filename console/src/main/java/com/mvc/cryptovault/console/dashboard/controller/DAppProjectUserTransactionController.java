package com.mvc.cryptovault.console.dashboard.controller;

import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiyichen
 * @create 2018/11/21 16:44
 */
@RestController
@RequestMapping("dashboard/appProjectUserTransaction")
public class DAppProjectUserTransactionController extends BaseController {
//    @Autowired
//    AppProjectUserTransactionService appProjectUserTransactionService;
//
//    @PutMapping("{id}")
//    public Result<Boolean> cancelProject(@PathVariable("id") BigInteger id) {
//        AppProjectUserTransaction appProjectUserTransaction = appProjectUserTransactionService.findById(id);
//        Assert.isTrue(appProjectUserTransaction.getResult() == 0, "无法取消");
//        appProjectUserTransaction.setResult(4);
//        appProjectUserTransactionService.update(appProjectUserTransaction);
//        appProjectUserTransactionService.updateAllCache();
//        appProjectUserTransactionService.updateCache(id);
//        return new Result<>(true);
//    }
//
//    @GetMapping("")
//    public Result<PageInfo<DProjectOrderVO>> findOrders(@ModelAttribute PageDTO pageDTO, @ModelAttribute DProjectOrderDTO dto) {
//        PageInfo<DProjectOrderVO> result = appProjectUserTransactionService.findOrders(dto);
//        return new Result<>(result);
//    }

}
