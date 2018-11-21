package com.mvc.cryptovault.dashboard.controller;

import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.OverTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTransactionVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.OverTransactionVO;
import com.mvc.cryptovault.common.permission.NotLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/19 19:50
 */
@RestController
@RequestMapping("transaction")
@Api(tags = "挂单交易相关")
public class TransactionController extends BaseController {

    @ApiOperation("挂单交易列表查询")
    @GetMapping
    public Result<DTransactionVO> findTransaction(@ModelAttribute @Valid PageDTO pageDTO, @ModelAttribute @Valid DTransactionDTO dTransactionDTO) {
        return null;
    }

    @ApiOperation("挂单交易撤销")
    @GetMapping("{id}")
    public Result<Boolean> cancel(@PathVariable BigInteger id) {
        return null;
    }

    @ApiOperation("挂单交易导出")
    @GetMapping("excel")
    @NotLogin
    public void transactionExport(@RequestParam String sign, @ModelAttribute @Valid PageDTO pageDTO, @ModelAttribute @Valid DTransactionDTO dTransactionDTO) {
        return;
    }

    @ApiOperation("成交记录查询")
    @GetMapping("over")
    public Result<OverTransactionVO> overList(@ModelAttribute @Valid OverTransactionDTO overTransactionDTO) {
        return null;
    }

    @ApiOperation("成交记录导出")
    @GetMapping("over/excel")
    @NotLogin
    public void overTransactionExport(@RequestParam String sign, @ModelAttribute @Valid PageDTO pageDTO, @ModelAttribute @Valid OverTransactionDTO overTransactionDTO) {
        return;
    }
}
