package com.mvc.cryptovault.explorer.controller;

import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.explorer.bean.PageDTO;
import com.mvc.cryptovault.explorer.service.BlockService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:51
 */
@RestController
@RequestMapping("block")
@Api(tags = "区块链相关操作")
public class BlockController extends BaseController {

    @Autowired
    BlockService blockService;

    @GetMapping("last")
    @ApiOperation("获取最新的区块信息")
    public Result<NowBlockVO> getLast() {
        NowBlockVO result = blockService.getLast();
        return new Result<>(result);
    }

    @GetMapping("transaction/last")
    @ApiOperation("获取最新交易")
    public Result<List<ExplorerTransactionSimpleVO>> getLastTransaction(@ModelAttribute PageDTO pageDTO) {
        List<ExplorerTransactionSimpleVO> result = blockService.getLastTransaction(pageDTO);
        return new Result<>(result);
    }


    @GetMapping()
    @ApiOperation("获取区块列表")
    public Result<List<ExplorerSimpleVO>> getBlocks(@RequestParam(required = false) BigInteger blockId, @ModelAttribute PageDTO pageDTO) {
        List<ExplorerSimpleVO> result = blockService.getBlocks(blockId, pageDTO);
        return new Result<>(result);
    }

    @GetMapping("{blockId}")
    @ApiOperation("获取区块详情")
    public Result<ExplorerSimpleVO> getBlockInfo(@PathVariable BigInteger blockId) {
        ExplorerSimpleVO result = blockService.getBlockInfo(blockId);
        return new Result<>(result);
    }


    @GetMapping("{blockId}/transactions")
    @ApiOperation("获取区块详情交易列表")
    public Result<List<ExplorerTransactionSimpleVO>> getBlockTransaction(@PathVariable(required = false) BigInteger blockId, @RequestParam(required = false) BigInteger transactionId, @ModelAttribute PageDTO pageDTO) {
        List<ExplorerTransactionSimpleVO> result = blockService.getBlockTransaction(blockId, transactionId, pageDTO);
        return new Result<>(result);
    }

    @GetMapping("transaction/tx/{hash}")
    @ApiOperation("根据hash查询交易详情")
    public Result<ExplorerTransactionDetailVO> getTxDetail(@PathVariable String hash) {
        ExplorerTransactionDetailVO result = blockService.getTxDetail(hash);
        return new Result<>(result);
    }

    @GetMapping("address/exist")
    @ApiOperation("查询公钥是否存在,存在则返回公钥,不存在返回空")
    public Result<String> publicKeyExist(@RequestParam String publicKey) {
        String result = blockService.publicKeyExist(publicKey);
        return new Result<>(result);
    }

    @GetMapping("address/balance")
    @ApiOperation("根据公钥查询资产")
    public Result<List<ExplorerBalanceVO>> getBalance(@RequestParam String publicKey) {
        List<ExplorerBalanceVO> result = blockService.getBalance(publicKey);
        return new Result<>(result);
    }

    @GetMapping("address/order")
    @ApiOperation("根据公钥查询订单列表")
    public Result<List<ExplorerSimpleOrder>> getOrders(@RequestParam String publicKey, @RequestParam(value = "id", required = false) BigInteger id, @ModelAttribute PageDTO pageDTO) {
        List<ExplorerSimpleOrder> result = blockService.getOrders(publicKey, pageDTO, id);
        return new Result<>(result);
    }

    @GetMapping("address/order/{id}")
    @ApiOperation("根据公钥查询订单详情")
    public Result<ExplorerOrder> getOrderDetail(@PathVariable BigInteger id) {
        ExplorerOrder order = blockService.getOrderDetail(id);
        return new Result<>(order);
    }

}