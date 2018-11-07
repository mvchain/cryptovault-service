package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.bean.dto.DebitDTO;
import com.mvc.cryptovault.app.bean.dto.TransactionDTO;
import com.mvc.cryptovault.app.bean.dto.TransactionSearchDTO;
import com.mvc.cryptovault.app.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.app.bean.vo.TransactionSimpleVO;
import com.mvc.cryptovault.app.bean.vo.TransactionTokenVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * 资产相关
 *
 * @author qiyichen
 * @create 2018/11/7 11:00
 */
@Api(tags = "资产相关")
@RequestMapping("asset")
@RestController
public class AssetController extends BaseController {

    @ApiOperation("获取余额,传入币种编号返回对应列表,建议缓存.币种名称图标等信息通过币种获取接口保存,这里不再重复返回")
    @GetMapping()
    @SwaggerMock("${asset.all}")
    public @ResponseBody Result<List<TokenBalanceVO>> getAsset() {
        return mockResult;
    }

    @ApiOperation("获取资产总值,观察列表中不存在但是余额存在的也会被统计.统一以USDT为单位返回,客户端根据币种自行转换.建议缓存")
    @GetMapping("balance")
    @SwaggerMock("${asset.balance}")
    public @ResponseBody Result<BigDecimal> getBalance() {
        return mockResult;
    }

    @ApiOperation("获取资产转账列表")
    @GetMapping("transactions")
    @SwaggerMock("${asset.transactions}")
    public Result<List<TransactionSimpleVO>> getTransactions(@ModelAttribute @Valid TransactionSearchDTO transactionSearchDTO) {
        return mockResult;
    }

    @ApiOperation("根据转账交易ID获取转账详情")
    @GetMapping("transaction/{id}")
    @SwaggerMock("${asset.transactionDetail}")
    public Result<TransactionSimpleVO> getTransaction(@PathVariable BigInteger id) {
        return mockResult;
    }

    @ApiOperation("根据币种缩写获取收款地址,不区分大小写,建议缓存")
    @GetMapping("address")
    @SwaggerMock("${asset.address}")
    public Result<String> getAddress(@RequestParam String tokenType) {
        return mockResult;
    }

    @ApiOperation("划账余额获取,没有可选币种,固定")
    @GetMapping("debit")
    @SwaggerMock("${asset.debitBalance}")
    public Result<BigDecimal> debit() {
        return mockResult;
    }

    @ApiOperation("划账,没有可选币种,固定,密码加密方法待定,预留出封装方法")
    @PostMapping("debit")
    @SwaggerMock("${asset.debit}")
    public Result<Boolean> debit(@RequestBody @Valid DebitDTO debitDTO) {
        return mockResult;
    }

    @ApiOperation("传入币种缩写获取转账信息，不区分大小写")
    @GetMapping("transaction")
    @SwaggerMock("${asset.transactionInfo}")
    public Result<TransactionTokenVO> getTransactionInfo(@RequestParam String tokenType) {
        return mockResult;
    }

    @ApiOperation("发起转账,密码加密方法待定,预留出封装方法")
    @PostMapping("transaction")
    @SwaggerMock("${asset.transaction}")
    public Result<Boolean> getTransactionInfo(@RequestBody @Valid TransactionDTO transactionDTO) {
        return mockResult;
    }

}
