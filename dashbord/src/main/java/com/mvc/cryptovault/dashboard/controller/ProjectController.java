package com.mvc.cryptovault.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectOrderDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectOrderVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectVO;
import com.mvc.cryptovault.common.permission.NotLogin;
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
 * @create 2018/11/19 19:49
 */
@RestController
@RequestMapping("project")
@Api(tags = "众筹项目相关")
public class ProjectController extends BaseController {

    private static LinkedHashMap<String, String> projectTransactionMap = new LinkedHashMap<>();

    @ApiOperation("项目列表查询")
    @GetMapping
    public Result<PageInfo<DProjectVO>> projects(@ModelAttribute @Valid PageDTO pageDTO) {
        PageInfo<DProjectVO> result = projectService.projects(pageDTO);
        return new Result<>(result);
    }

    @ApiOperation("项目明细查询")
    @GetMapping("{id}")
    public Result<DProjectDetailVO> getDetail(@PathVariable BigInteger id) {
        DProjectDetailVO result = projectService.getDetail(id);
        return new Result<>(result);
    }

    @ApiOperation("项目新建")
    @PostMapping
    public Result<Boolean> newProject(@RequestBody @Valid DProjectDTO dProjectDTO) {
        Boolean result = projectService.newProject(dProjectDTO);
        return new Result<>(result);
    }

    @ApiOperation("项目编辑")
    @PutMapping
    public Result<Boolean> updateProject(@RequestBody @Valid DProjectDTO dProjectDTO) {
        Boolean result = projectService.updateProject(dProjectDTO);
        return new Result<>(result);
    }

    @ApiOperation("项目删除")
    @DeleteMapping("{id}")
    public Result<Boolean> deleteProject(@PathVariable BigInteger id) {
        Boolean result = projectService.deleteProject(id);
        return new Result<>(result);
    }

    @ApiOperation("项目预约订单查询")
    @GetMapping("order")
    public Result<PageInfo<DProjectOrderVO>> findOrders(@ModelAttribute @Valid DProjectOrderDTO dto) {
        PageInfo<DProjectOrderVO> result = projectService.findOrders(dto);
        return new Result<>(result);
    }


    @ApiOperation("项目预约订单取消")
    @DeleteMapping("order/{id}")
    public Result<Boolean> cancelOrder(@PathVariable BigInteger id) {
        Boolean result = projectService.cancel(id);
        return new Result<>(result);
    }

    @ApiOperation("预约订单导出")
    @NotLogin
    @GetMapping("order/excel")
    public void overTransactionExport(HttpServletResponse response, @RequestParam String sign, @ModelAttribute @Valid DProjectOrderDTO dto) throws IOException, ExcelException {
        getUserIdBySign(sign);
        PageInfo<DProjectOrderVO> result = projectService.findOrders(dto);
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("project_transaction_%s.xls", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        ExcelUtil.listToExcel(result.getList(), getProjectTransactionMap(), "ProjectTransactionTable", os);
    }

    private LinkedHashMap<String, String> getProjectTransactionMap() {
        if (projectTransactionMap.isEmpty()) {
            projectTransactionMap.put("createdAt", "预约时间");
            projectTransactionMap.put("orderNumber", "订单号");
            projectTransactionMap.put("projectName", "项目名称");
            projectTransactionMap.put("cellphone", "用户手机号");
            projectTransactionMap.put("successValue", "成功数量");
            projectTransactionMap.put("value", "预约数量");
            projectTransactionMap.put("successPayed", "最终支付金额");
            projectTransactionMap.put("payed", "预约金额");
            projectTransactionMap.put("statusStr", "订单状态");
            projectTransactionMap.put("projectStatusStr", "项目状态");
        }
        return projectTransactionMap;
    }

}
