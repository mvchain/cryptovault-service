package com.mvc.cryptovault.app.feign;

import com.mvc.cryptovault.common.bean.dto.MyTransactionDTO;
import com.mvc.cryptovault.common.bean.dto.OrderDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@FeignClient("console")
public interface TransactionRemoteService {

    @GetMapping("commonPair")
    Result<List<PairVO>> getPair(@RequestParam("pairType") Integer pairType);

    @GetMapping("appUserTransaction")
    Result<List<OrderVO>> getTransactions(@ModelAttribute OrderDTO dto);

    @GetMapping("appKline")
    Result<KLineVO> getTransactions(@RequestParam("pairId") BigInteger pairId);

    @GetMapping("appUserTransaction/userId/{userId}")
    Result<List<MyOrderVO>> getUserTransactions(@PathVariable("userId") BigInteger userId, @ModelAttribute MyTransactionDTO dto);

    @PostMapping("appUserTransaction/userId/{userId}")
    Result<Boolean> buy(@PathVariable("userId") BigInteger userId, @RequestBody TransactionBuyDTO dto);

    @GetMapping("commonPair/userId/{userId}")
    Result<OrderInfoVO> getInfo(@PathVariable("userId") BigInteger userId, @RequestParam("pairId") BigInteger pairId, @RequestParam Integer transactionType);

    @PutMapping("appUserTransaction/userId/{userId}")
    Result<Boolean> cancel(@PathVariable("userId") BigInteger userId, @RequestParam("id") BigInteger id);

}
