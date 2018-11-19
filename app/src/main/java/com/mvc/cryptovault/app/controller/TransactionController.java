package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.common.bean.dto.MyTransactionDTO;
import com.mvc.cryptovault.common.bean.dto.OrderDTO;
import com.mvc.cryptovault.common.bean.dto.PairDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

/**
 * 本项目不涉及撮合交易
 *
 * @author qiyichen
 * @create 2018/11/7 17:26
 */
@RestController
@Api(tags = "交易相关")
@RequestMapping("transaction")
public class TransactionController extends BaseController {

    @ApiOperation("获取交易对,很少变动,本地必须缓存")
    @GetMapping("pair")
    @SwaggerMock("${transaction.pair}")
    public Result<List<PairVO>> getPair(@ModelAttribute @Valid PairDTO pairDTO) {
        return new Result<>(transactionService.getPair(getUserId(), pairDTO));
    }

    @ApiOperation("获取挂单列表")
    @GetMapping()
    @SwaggerMock("${transaction.list}")
    public Result<List<OrderVO>> getTransactions(@ModelAttribute OrderDTO dto) {
        return new Result<>(transactionService.getTransactions(getUserId(), dto));
    }

    @ApiOperation("获取7日交易K线")
    @GetMapping("pair/kline")
    @SwaggerMock("${transaction.kline}")
    public Result<KLineVO> getKLine(@RequestParam BigInteger pairId) {
        return new Result<>(transactionService.getKLine(getUserId(), pairId));
    }

    @ApiOperation("筛选已参与订单")
    @GetMapping("partake")
    @SwaggerMock("${transaction.all}")
    public Result<List<MyOrderVO>> getUserTransactions(@ModelAttribute @Valid MyTransactionDTO dto) {
        return new Result<>(transactionService.getUserTransactions(getUserId(), dto));
    }

    @ApiOperation("发起挂单")
    @PostMapping("")
    @SwaggerMock("${transaction.buy}")
    public Result<Boolean> buy(@RequestBody TransactionBuyDTO dto) {
        return new Result<>(transactionService.buy(getUserId(), dto));
    }

    @ApiOperation("挂单信息获取transactionType:1购买 2出售")
    @GetMapping("info")
    @SwaggerMock("${transacction.info}")
    public Result<OrderInfoVO> getInfo(@RequestParam BigInteger pairId, @RequestParam Integer transactionType) {
        return new Result<>(transactionService.getInfo(getUserId(), pairId, transactionType));
    }

    @ApiOperation("取消挂单")
    @DeleteMapping("{id}")
    @SwaggerMock("${transaction.cancel}")
    public Result<Boolean> cancel(@PathVariable BigInteger id) {
        return new Result<>(transactionService.cancel(getUserId(), id));
    }

}