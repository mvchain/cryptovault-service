package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.bean.dto.PageDTO;
import com.mvc.cryptovault.app.service.FinancialService;
import com.mvc.cryptovault.common.bean.dto.FinancialBuyDTO;
import com.mvc.cryptovault.common.bean.dto.FinancialPartakeDTO;
import com.mvc.cryptovault.common.bean.dto.FinancialPartakeListDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2019/1/9 11:06
 */
@Api(tags = "理财相关")
@RestController
@RequestMapping("financial")
public class FinancialController extends BaseController {

    @Autowired
    FinancialService financialService;

    @ApiOperation("理财产品列表")
    @GetMapping
    public Result<List<FinancialSimpleVO>> getList(@ModelAttribute PageDTO pageDTO, @RequestParam(required = false) BigInteger id) {
        return new Result<>(financialService.getList(pageDTO, id));
    }

    @ApiOperation("持仓列表")
    @GetMapping("partake")
    public Result<List<FinancialUserPartakeVO>> getPartakeList(@ModelAttribute FinancialPartakeDTO financialPartakeDTO) {
        List<FinancialUserPartakeVO> result = financialService.getFinancialPartakeList(financialPartakeDTO, getUserId());
        return new Result<>(result);
    }

    @ApiOperation("理财资产统计")
    @GetMapping("balance")
    public Result<FinancialBalanceVO> getFinancialBalance() {
        return new Result<>(financialService.getFinancialBalance(getUserId()));
    }

    @ApiOperation("理财详情查看(购买时详情获取)")
    @GetMapping("{id}")
    public Result<FinancialDetailVO> getDetail(@PathVariable BigInteger id) {
        return new Result<>(financialService.getFinancialDetail(id, getUserId()));
    }

    @ApiOperation("某理财持仓详情(传入理财记录id)")
    @GetMapping("partake/{id}")
    public Result<FinancialPartakeDetailVO> getPartakeDetail(@PathVariable BigInteger id) {
        return new Result<>(financialService.getPartakeDetail(id, getUserId()));
    }

    @ApiOperation("根据理财记录id持仓收益列表详情")
    @GetMapping("partake/{partakeId}/detail")
    public Result<List<FinancialPartakeListVO>> getPartakeList(@PathVariable BigInteger partakeId, @ModelAttribute FinancialPartakeListDTO financialPartakeListDTO) {
        return new Result<>(financialService.getPartakeList(partakeId, financialPartakeListDTO, getUserId()));
    }

    @ApiOperation("根据理财项目id购买理财")
    @PostMapping("{id}")
    public Result<Boolean> buy(@PathVariable BigInteger id, @RequestBody FinancialBuyDTO financialBuyDTO) {
        return new Result<>(financialService.buy(id, financialBuyDTO, getUserId()));
    }

    @ApiOperation("根据理财记录id提取理财收益")
    @PostMapping("partake/{id}")
    public Result<Boolean> unlockPartake(@PathVariable BigInteger id) {
        return new Result<>(financialService.unlockPartake(id, getUserId()));
    }

}
