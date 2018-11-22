package com.mvc.cryptovault.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockStatusDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockeTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DBlockeTransactionVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DHoldVO;
import com.mvc.cryptovault.common.permission.NotLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
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


    @ApiOperation("保留金额获取,不分页")
    @GetMapping("hold")
    public Result<List<DHoldVO>> getHold() {
        List<DHoldVO> result = blockService.getHold();
        return new Result<>(result);
    }

    @ApiOperation("保留金额设置（仅对eth）")
    @PutMapping("hold")
    public Result<Boolean> setHold(@RequestBody List<DHoldVO> list) {
        Boolean result = blockService.setHold(list);
        return new Result<>(result);
    }

    @ApiOperation("手续费列表获取")
    @GetMapping("fee")
    public Result<List<DHoldVO>> getFee() {
        List<DHoldVO> result = blockService.getFee();
        return new Result<>(result);
    }

    @ApiOperation("手续费设置")
    @PutMapping("fee")
    public Result<List<DHoldVO>> setFee(@RequestBody List<DHoldVO> list) {
        Boolean result = blockService.setFee(list);
        return new Result<>(list);
    }

    @ApiOperation("区块链交易查询")
    @GetMapping("transactions")
    public Result<PageInfo<DBlockeTransactionVO>> getTransactions(@ModelAttribute @Valid DBlockeTransactionDTO dBlockeTransactionDTO) {
        PageInfo<DBlockeTransactionVO> result = blockService.getTransactions(dBlockeTransactionDTO);
        return new Result<>(result);
    }

    @ApiOperation("区块链交易导出")
    @NotLogin
    @GetMapping("transactions/excel")
    public void getTransactionsExcel(@RequestParam String sign, @ModelAttribute @Valid PageDTO pageDTO, @ModelAttribute @Valid DBlockeTransactionDTO dBlockeTransactionDTO) {
        return;
    }

    @ApiOperation("账户余额查看")
    @GetMapping("balance/{tokeId}")
    public Result<BigDecimal> getBalance(@PathVariable BigInteger tokenId) {
        BigDecimal result = blockService.getBalance(tokenId);
        return new Result<>(result);
    }


    @ApiOperation("批量操作(1同意 2拒绝)")
    @PutMapping("status")
    public Result<Boolean> updateStatus(DBlockStatusDTO dBlockStatusDTO) {
        Boolean result = blockService.updateStatus(dBlockStatusDTO);
        return new Result<>(result);
    }

    @ApiOperation("待签名数据导出")
    @GetMapping("transaction/export")
    @NotLogin
    public void exportSign() throws IOException {
    }

    @ApiOperation("导入签名数据")
    @PostMapping("sign/import")
    public Result<Boolean> importSign(@RequestBody MultipartFile file) throws IOException {
        return null;
    }

    @ApiOperation("待汇总数据导出")
    @NotLogin
    @GetMapping("collect/export")
    public void exportSign(HttpServletResponse response, @RequestParam String sign) throws IOException {
    }

    @ApiOperation("导入账户")
    @PostMapping("account/import")
    public Result<Boolean> importAccount(@RequestBody MultipartFile file) throws IOException {
        return null;
    }

    @ApiOperation("账户库存数据获取")
    @GetMapping("account/count")
    public Result<Integer> accountCount(@RequestParam String tokenType) {
        Integer result = blockService.accountCount(tokenType);
        return new Result<>(result);
    }
}
