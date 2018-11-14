package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/14 14:42
 */
@RequestMapping("blockTransaction")
public class BlockTransactionController extends BaseController {

    @PostMapping("{userId}")
    public Result<Boolean> sendTransaction(@PathVariable("userId") BigInteger userId, @RequestBody TransactionDTO transactionDTO) {
        blockTransactionService.sendTransaction(userId, transactionDTO);
        return new Result<>(true);
    }

}
