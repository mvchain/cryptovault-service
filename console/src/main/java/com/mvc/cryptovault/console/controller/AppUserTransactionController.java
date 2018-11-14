package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.MyTransactionDTO;
import com.mvc.cryptovault.common.bean.dto.OrderDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.bean.vo.MyOrderVO;
import com.mvc.cryptovault.common.bean.vo.OrderVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * TODO 数据量大，需要优化
 *
 * @author qiyichen
 * @create 2018/11/14 14:34
 */
@RequestMapping("appUserTransaction")
public class AppUserTransactionController extends BaseController {

    @GetMapping()
    public Result<List<OrderVO>> getTransactions(@ModelAttribute OrderDTO dto) {
        List<OrderVO> result = appUserTransactionService.getTransactions(dto);
        return new Result<>(result);
    }


    @GetMapping("userId/{userId}")
    public Result<List<MyOrderVO>> getUserTransactions(@PathVariable("userId") BigInteger userId, @ModelAttribute MyTransactionDTO dto) {
        List<MyOrderVO> result = appUserTransactionService.getUserTransactions(userId, dto);
        return new Result<>(result);
    }


    @PostMapping("userId/{userId}")
    public Result<Boolean> buy(@PathVariable("userId") BigInteger userId, @RequestBody TransactionBuyDTO dto) {
        AppUser user = appUserService.findById(userId);
        Assert.isTrue(user.getTransactionPassword().equalsIgnoreCase(dto.getPassword()), MessageConstants.getMsg("USER_TRANS_PASS_WRONG"));
        appUserTransactionService.buy(userId, dto);
        return new Result<>(true);
    }


    @PutMapping("userId/{userId}")
    public Result<Boolean> cancel(@PathVariable("userId") BigInteger userId, @RequestParam("id") BigInteger id) {
        appUserTransactionService.cancel(userId, id);
        return new Result<>(true);
    }

}
