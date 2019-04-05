package com.mvc.cryptovault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppFinancial;
import com.mvc.cryptovault.common.bean.dto.AppFinancialDTO;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.AppFinancialDetailVO;
import com.mvc.cryptovault.common.bean.vo.AppFinancialOrderVO;
import com.mvc.cryptovault.common.bean.vo.AppFinancialVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppUserFinancialPartakeService;
import com.mvc.cryptovault.console.service.FinancialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiyichen
 * @create 2018/11/21 16:37
 */
@RestController
@RequestMapping("dashboard/financial")
public class DFinancialController extends BaseController {

    @Autowired
    FinancialService financialService;
    @Autowired
    AppUserFinancialPartakeService partakeService;

    @GetMapping("")
    public Result<PageInfo<AppFinancialVO>> getFinancialList(@ModelAttribute PageDTO pageDTO, @RequestParam(value = "financialName", required = false) String financialName) {
        List<AppFinancial> list = financialService.getDFinancialList(pageDTO, financialName);
        PageInfo result = new PageInfo<>(list);
        result.setList(list.stream().map(obj -> {
            AppFinancialVO vo = new AppFinancialVO();
            BeanUtils.copyProperties(obj, vo);
            vo.setNextIncome(Float.valueOf(partakeService.getIncomeNextDay(obj)));
            return vo;
        }).collect(Collectors.toList()));
        return new Result<>(result);
    }

    @GetMapping("{id}")
    public Result<AppFinancialDetailVO> getFinancialDetail(@PathVariable("id") BigInteger id) {
        AppFinancialDetailVO obj = financialService.getDFinancialDetail(id);
        return new Result<>(obj);
    }

    @PostMapping("")
    public Result<Boolean> saveAppFinancial(@RequestBody AppFinancialDTO appFinancialDTO) {
        financialService.saveAppFinancial(appFinancialDTO);
        return new Result<>(true);
    }

    @PutMapping("")
    public Result<Boolean> updateAppFinancial(@RequestBody AppFinancialDTO appFinancialDTO) {
        financialService.updateAppFinancial(appFinancialDTO);
        return new Result<>(true);
    }

    @GetMapping("{id}/order")
    public Result<PageInfo<AppFinancialOrderVO>> getFinancialOrderList(@PathVariable("id") BigInteger id, @ModelAttribute PageDTO pageDTO, @RequestParam(value = "searchKey", required = false) String searchKey, @RequestParam(value = "status", required = false) Integer status) {
        PageInfo<AppFinancialOrderVO> result = financialService.getFinancialOrderList(id, pageDTO, searchKey, status);
        return new Result<>(result);
    }

}
