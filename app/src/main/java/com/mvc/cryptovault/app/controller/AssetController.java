package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.common.bean.dto.AssertVisibleDTO;
import com.mvc.cryptovault.common.bean.dto.DebitDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionSearchDTO;
import com.mvc.cryptovault.common.bean.vo.*;
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

    @ApiOperation("获取余额,建议缓存.币种名称图标等信息通过币种获取接口保存,这里不再重复返回.返回需要更具本地列表自行匹配,返回结果无序")
    @GetMapping()
    @SwaggerMock("${asset.all}")
    public @ResponseBody
    Result<List<TokenBalanceVO>> getAsset() {
        return new Result<>(assetService.getAsset(getUserId()));
    }

    @ApiOperation("设置币种展示开关,更新优先队列较低")
    @PutMapping
    public @ResponseBody
    Result<Boolean> updateVisible(@RequestBody @Valid AssertVisibleDTO assertVisibleDTO) {
        return new Result<>(assetService.updateVisible(getUserId(), assertVisibleDTO));
    }

    @ApiOperation("获取资产总值,观察列表中不存在但是余额存在的也会被统计.统一以CNY为单位返回,客户端根据币种自行转换.建议缓存")
    @GetMapping("balance")
    @SwaggerMock("${asset.balance}")
    public @ResponseBody
    Result<BigDecimal> getBalance() {
        return new Result<>(assetService.getBalance(getUserId()));
    }

    @ApiOperation("获取资产转账列表")
    @GetMapping("transactions")
    @SwaggerMock("${asset.transactions}")
    public Result<List<TransactionSimpleVO>> getTransactions(@ModelAttribute @Valid TransactionSearchDTO transactionSearchDTO) {
        return new Result<>(assetService.getTransactions(getUserId(), transactionSearchDTO));
    }

    @ApiOperation("根据转账交易ID获取转账详情")
    @GetMapping("transaction/{id}")
    @SwaggerMock("${asset.transactionDetail}")
    public Result<TransactionDetailVO> getTransaction(@PathVariable BigInteger id) {
        return new Result<>(assetService.getTransaction(getUserId(), id));
    }

    @ApiOperation("根据币种id获取收款地址,建议缓存")
    @GetMapping("address")
    @SwaggerMock("${asset.address}")
    public Result<String> getAddress(@RequestParam BigInteger tokenId) {
        return new Result<>(assetService.getAddress(getUserId(), tokenId));
    }

    @ApiOperation("划账余额获取,没有可选币种,固定")
    @GetMapping("debit")
    @SwaggerMock("${asset.debitBalance}")
    public Result<BigDecimal> debitBalance() {
        return new Result<>(assetService.debit(getUserId()));
    }

    @ApiOperation("划账,没有可选币种,固定,密码加密方法待定,预留出封装方法")
    @PostMapping("debit")
    @SwaggerMock("${asset.debit}")
    public Result<Boolean> debit(@RequestBody @Valid DebitDTO debitDTO) {
        return new Result<>(assetService.debit(getUserId(), debitDTO));
    }

    @ApiOperation("传入币种id取转账所需信息，不区分大小写")
    @GetMapping("transaction")
    @SwaggerMock("${asset.transactionInfo}")
    public Result<TransactionTokenVO> getTransactionInfo(@RequestParam BigInteger tokenId) {
        return new Result<>(assetService.getTransactionInfo(getUserId(), tokenId));
    }

    @ApiOperation("发起转账,密码加密方法待定,预留出封装方法,需要得知vpay的加密方式。其他密码相关不重复描述")
    @PostMapping("transaction")
    @SwaggerMock("${asset.transaction}")
    public Result<Boolean> sendTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        return new Result<>(assetService.sendTransaction(getUserId(), transactionDTO));
    }

}
