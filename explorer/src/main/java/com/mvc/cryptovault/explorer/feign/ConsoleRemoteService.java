package com.mvc.cryptovault.explorer.feign;

import com.mvc.cryptovault.common.bean.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.List;

@FeignClient("console")
public interface ConsoleRemoteService {

    @GetMapping("explorer/last")
    Result<NowBlockVO> getLast();

    @GetMapping("explorer/transaction/last")
    Result<List<ExplorerTransactionSimpleVO>> getLastTransaction(@RequestParam("pageSize") Integer pageSize);

    @GetMapping("explorer")
    Result<List<ExplorerSimpleVO>> getBlocks(@RequestParam(required = false, value = "blockId") BigInteger blockId, @RequestParam("pageSize") Integer pageSize);

    @GetMapping("explorer/{blockId}")
    Result<ExplorerSimpleVO> getBlockInfo(@PathVariable("blockId") BigInteger blockId);

    @GetMapping("explorer/{blockId}/transactions")
    Result<List<ExplorerTransactionSimpleVO>> getBlockTransaction(@PathVariable("blockId") BigInteger blockId, @RequestParam(value = "transactionId", required = false) BigInteger transactionId, @RequestParam("pageSize") Integer pageSize);

    @GetMapping("explorer/transaction/tx/{hash}")
    Result<ExplorerTransactionDetailVO> getTxDetail(@PathVariable("hash") String hash);

    @GetMapping("explorer/address/exist")
    Result<String> publicKeyExist(@RequestParam("publicKey") String publicKey);

    @GetMapping("explorer/address/balance")
    Result<List<ExplorerBalanceVO>> getBalance(@RequestParam("publicKey") String publicKey);

    @GetMapping("explorer/address/order")
    Result<List<ExplorerSimpleOrder>> getOrders(@RequestParam("publicKey") String publicKey, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "id", required = false) BigInteger id);

    @GetMapping("explorer/address/order/{id}")
    Result<ExplorerOrder> getOrderDetail(@PathVariable("id") BigInteger id);
}
