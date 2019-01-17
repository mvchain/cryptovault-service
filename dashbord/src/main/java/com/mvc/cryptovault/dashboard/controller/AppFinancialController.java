package com.mvc.cryptovault.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppFinancial;
import com.mvc.cryptovault.common.bean.dto.AppFinancialDTO;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.AppFinancialDetailVO;
import com.mvc.cryptovault.common.bean.vo.AppFinancialOrderVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.permission.NotLogin;
import com.mvc.cryptovault.common.permission.PermissionCheck;
import com.mvc.cryptovault.dashboard.service.AppFinancialService;
import com.mvc.cryptovault.dashboard.util.ExcelException;
import com.mvc.cryptovault.dashboard.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.LinkedHashMap;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@Api(tags = "理财项目相关")
@RestController
@RequestMapping("financial")
public class AppFinancialController extends BaseController {

    @Autowired
    private AppFinancialService appFinancialService;
    private static LinkedHashMap<String, String> financialOrderMap = new LinkedHashMap<>();

    @ApiOperation("查询理财产品列表")
    @GetMapping()
    public Result<PageInfo<AppFinancial>> getList(@ModelAttribute @Valid PageDTO pageDTO, @RequestParam(required = false) String financialName) {
        PageInfo<AppFinancial> result = appFinancialService.getFinancialList(pageDTO, financialName);
        return new Result<>(result);
    }

    @ApiOperation("获取理财项目详情")
    @GetMapping("{id}")
    public Result<AppFinancialDetailVO> getFinancialDetail(@PathVariable BigInteger id) {
        AppFinancialDetailVO result = appFinancialService.getFinancialDetail(id);
        return new Result<>(result);
    }

    @ApiOperation("新建理财产品")
    @PermissionCheck("7")
    @PostMapping()
    public Result<Boolean> saveAppFinancial(@RequestBody @Valid AppFinancialDTO appFinancialDTO) {
        appFinancialDTO.setId(null);
        appFinancialService.saveAppFinancial(appFinancialDTO);
        return new Result<>(true);
    }

    @ApiOperation("修改理财产品")
    @PermissionCheck("7")
    @PutMapping
    public Result<Boolean> updateAppFinancial(@RequestBody @Valid AppFinancialDTO appFinancialDTO) {
        appFinancialService.updateAppFinancial(appFinancialDTO);
        return new Result<>(true);
    }

    @ApiOperation("查询理财订单列表")
    @GetMapping("{id}/order")
    public Result<PageInfo<AppFinancialOrderVO>> getFinancialOrderList(@PathVariable BigInteger id, @ModelAttribute @Valid PageDTO pageDTO, @RequestParam(required = false) String searchKey, @RequestParam(value = "status", required = false) Integer status) {
        PageInfo<AppFinancialOrderVO> result = appFinancialService.getFinancialOrderList(id, pageDTO, searchKey, status);
        return new Result<>(result);
    }


    @ApiOperation("查询理财订单列表导出,全部导出时分页参数设置最大即可(excel单表最大65534行)")
    @GetMapping("{id}/order/excel")
    @NotLogin
    public void getFinancialOrderListExport(HttpServletResponse response, @RequestParam String sign, @PathVariable BigInteger id, @ModelAttribute @Valid PageDTO pageDTO, @RequestParam(required = false) String searchKey, @RequestParam(value = "status", required = false) Integer status) throws IOException, ExcelException {
        getUserIdBySign(sign);
        PageInfo<AppFinancialOrderVO> result = appFinancialService.getFinancialOrderList(id, pageDTO, searchKey, status);
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("financial_order_%s.xls", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        ExcelUtil.listToExcel(result.getList(), getFinancialOrderMap(), "FinancialOrderTable", os);
    }

    private LinkedHashMap<String, String> getFinancialOrderMap() {
        if (financialOrderMap.isEmpty()) {
            financialOrderMap.put("orderNumber", "订单号");
            financialOrderMap.put("nickname", "用户名");
            financialOrderMap.put("email", "邮箱");
            financialOrderMap.put("statusStr", "状态");
            financialOrderMap.put("financialName", "理财项目");
            financialOrderMap.put("createdAt", "购买时间");
            financialOrderMap.put("valueStr", "持仓本金");
            financialOrderMap.put("incomeStr", "累计收益");
            financialOrderMap.put("priceStr", "价值");
        }
        return financialOrderMap;
    }

}
