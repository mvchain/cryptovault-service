package com.mvc.cryptovault.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.OverTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTransactionVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.OverTransactionVO;
import com.mvc.cryptovault.common.permission.NotLogin;
import com.mvc.cryptovault.common.permission.PermissionCheck;
import com.mvc.cryptovault.dashboard.util.ExcelException;
import com.mvc.cryptovault.dashboard.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.LinkedHashMap;

/**
 * @author qiyichen
 * @create 2018/11/19 19:50
 */
@RestController
@RequestMapping("transaction")
@Api(tags = "挂单交易相关")
public class TransactionController extends BaseController {

    private static LinkedHashMap<String, String> transactionMap = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> overMap = new LinkedHashMap<>();

    @ApiOperation("挂单交易列表查询")
    @GetMapping
    public Result<PageInfo<DTransactionVO>> findTransaction(@ModelAttribute @Valid DTransactionDTO dTransactionDTO) {
        PageInfo<DTransactionVO> result = transactionService.findTransaction(dTransactionDTO);
        return new Result<>(result);
    }

    @ApiOperation("挂单交易撤销")
    @DeleteMapping("{id}")
    @PermissionCheck("6")
    public Result<Boolean> cancel(@PathVariable BigInteger id) {
        Boolean result = transactionService.cancelTransaction(id);
        return new Result<>(result);
    }

    @ApiOperation("挂单交易导出")
    @GetMapping("excel")
    @NotLogin
    public void transactionExport(HttpServletResponse response, @RequestParam String sign, @ModelAttribute @Valid DTransactionDTO dTransactionDTO) throws IOException, ExcelException {
        getUserIdBySign(sign);
        PageInfo<DTransactionVO> result = transactionService.findTransaction(dTransactionDTO);
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("transaction_%s.xls", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        ExcelUtil.listToExcel(result.getList(), getTransactionMap(), "TransactionTable", os);
    }

    private LinkedHashMap<String, String> getTransactionMap() {
        if (transactionMap.isEmpty()) {
            transactionMap.put("createdAt", "挂单时间");
            transactionMap.put("orderNumber", "订单号");
            transactionMap.put("cellphone", "挂单用户手机号");
            transactionMap.put("transactionTypeStr", "交易类型");
            transactionMap.put("pairName", "交易对");
            transactionMap.put("value", "挂单数量");
            transactionMap.put("deal", "已成交数量");
            transactionMap.put("surplus", "待成交数量");
            transactionMap.put("price", "价格");
            transactionMap.put("statusStr", "状态");
        }
        return transactionMap;
    }


    private LinkedHashMap<String, String> getOverMap() {
        if (overMap.isEmpty()) {
            overMap.put("createdAt", "交易时间");
            overMap.put("orderNumber", "订单号");
            overMap.put("parentOrderNumber", "父订单号");
            overMap.put("cellphone", "交易用户手机号");
            overMap.put("transactionTypeStr", "交易类型");
            overMap.put("pairName", "交易对");
            overMap.put("value", "交易数量");
            overMap.put("price", "价格");
        }
        return overMap;
    }

    @ApiOperation("成交记录查询")
    @GetMapping("over")
    public Result<PageInfo<OverTransactionVO>> overList(@ModelAttribute @Valid OverTransactionDTO overTransactionDTO) {
        PageInfo<OverTransactionVO> result = tokenService.overList(overTransactionDTO);
        return new Result<>(result);
    }

    @ApiOperation("成交记录导出")
    @GetMapping("over/excel")
    @NotLogin
    public void overTransactionExport(HttpServletResponse response, @RequestParam String sign, @ModelAttribute @Valid OverTransactionDTO overTransactionDTO) throws IOException, ExcelException {
        getUserIdBySign(sign);
        PageInfo<OverTransactionVO> result = tokenService.overList(overTransactionDTO);
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("over_%s.xls", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        ExcelUtil.listToExcel(result.getList(), getOverMap(), "OverTable", os);
    }
}
