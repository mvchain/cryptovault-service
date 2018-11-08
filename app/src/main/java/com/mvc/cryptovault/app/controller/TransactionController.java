package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.bean.dto.MyTransactionDTO;
import com.mvc.cryptovault.app.bean.dto.OrderDTO;
import com.mvc.cryptovault.app.bean.dto.PairDTO;
import com.mvc.cryptovault.app.bean.dto.TransactionBuyDTO;
import com.mvc.cryptovault.app.bean.vo.*;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 17:26
 */
@RestController
@Api("交易相关")
@RequestMapping("transacion")
public class TransactionController extends BaseController {

    @ApiOperation("获取交易对,传入时间戳,很少变动,本地必须缓存")
    @GetMapping("pair")
    @SwaggerMock("${transaction.pair}")
    public Result<PairVO> getPair(@ModelAttribute @Valid PairDTO pairDTO) {
        return mockResult;
    }

    @ApiOperation("获取挂单列表")
    @GetMapping()
    @SwaggerMock("${transaction.list}")
    public Result<OrderVO> getTransactions(@ModelAttribute OrderDTO dto) {
        return mockResult;
    }

    @ApiOperation("获取7日交易K线")
    @GetMapping("pair/kline")
    @SwaggerMock("${transaction.kline}")
    public Result<KLineVO> getKLine(@RequestParam String pair) {
        return mockResult;
    }

    @ApiOperation("筛选已参与订单")
    @GetMapping("partake")
    @SwaggerMock("${transaction.all}")
    public Result<MyOrderVO> getUserTransactions(@ModelAttribute @Valid MyTransactionDTO dto) {
        return mockResult;
    }

    @ApiOperation("发起挂单")
    @PostMapping("")
    @SwaggerMock("${transaction.buy}")
    public Result<Boolean> buy(TransactionBuyDTO dto) {
        return mockResult;
    }

    @ApiOperation("挂单信息获取")
    @GetMapping("info")
    @SwaggerMock("${transacction.info}")
    public Result<OrderInfoVO> getInfo(@RequestParam String pair) {
        return mockResult;
    }

    @ApiOperation("取消挂单")
    @DeleteMapping("{id}")
    @SwaggerMock("${transaction.cancel}")
    public Result<Boolean> cancel(@PathVariable BigInteger id) {
        return mockResult;
    }

}
