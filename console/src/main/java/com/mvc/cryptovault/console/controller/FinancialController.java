package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.dto.FinancialBuyDTO;
import com.mvc.cryptovault.common.bean.dto.FinancialPartakeDTO;
import com.mvc.cryptovault.common.bean.dto.FinancialPartakeListDTO;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/14 14:35
 */
@RestController
@RequestMapping("financial")
public class FinancialController extends BaseController {

    @Autowired
    FinancialService financialService;

    @GetMapping("")
    public Result<List<FinancialSimpleVO>> getFinancialList(@ModelAttribute PageDTO pageDTO, @RequestParam(required = false) BigInteger id) {
        List<FinancialSimpleVO> result = financialService.getFinancialList(pageDTO, id);
        return new Result<>(result);
    }

    @GetMapping("partake")
    public Result<List<FinancialUserPartakeVO>> getFinancialPartakeList(@ModelAttribute FinancialPartakeDTO financialPartakeDTO, @RequestParam("userId") BigInteger userId) {
        List<FinancialUserPartakeVO> result = financialService.getFinancialPartakeList(financialPartakeDTO, userId);
        return new Result<>(result);
    }

    @GetMapping("balance")
    public Result<FinancialBalanceVO> getFinancialBalance(@RequestParam("userId") BigInteger userId) {
        FinancialBalanceVO result = financialService.getFinancialBalance(userId);
        return new Result<>(result);
    }

    @GetMapping("{id}")
    public Result<FinancialDetailVO> getFinancialDetail(@PathVariable("id") BigInteger id, @RequestParam("userId") BigInteger userId) {
        FinancialDetailVO result = financialService.getFinancialDetail(id, userId);
        return new Result<>(result);
    }

    @GetMapping("partake/{id}")
    public Result<FinancialPartakeDetailVO> getPartakeDetail(@PathVariable("id") BigInteger id, @RequestParam("userId") BigInteger userId) {
        FinancialPartakeDetailVO result = financialService.getPartakeDetail(id, userId);
        return new Result<>(result);
    }

    @GetMapping("partake/{id}/detail")
    public Result<List<FinancialPartakeListVO>> getPartakeList(@PathVariable("id") BigInteger financialId, @ModelAttribute FinancialPartakeListDTO dto, @RequestParam("userId") BigInteger userId) {
        List<FinancialPartakeListVO> result = financialService.getPartakeList(financialId, dto, userId);
        return new Result<>(result);
    }

    @PostMapping("{id}")
    public Result<Boolean> buyFinancial(@PathVariable("id") BigInteger id, @RequestBody FinancialBuyDTO financialBuyDTO, @RequestParam("userId") BigInteger userId) {
        Boolean result = financialService.buyFinancial(id, financialBuyDTO, userId);
        return new Result<>(result);
    }

    @PostMapping("partake/{id}")
    public Result<Boolean> unlockPartake(@PathVariable("id") BigInteger id, @RequestParam("userId") BigInteger userId) {
        Boolean result = financialService.unlockPartake(id, userId);
        return new Result<>(result);
    }

}
