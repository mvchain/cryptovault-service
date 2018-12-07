package com.mvc.cryptovault.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.ImportPartake;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.ExportPartake;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DProjectOrderDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectOrderVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DProjectVO;
import com.mvc.cryptovault.common.permission.NotLogin;
import com.mvc.cryptovault.common.permission.PermissionCheck;
import com.mvc.cryptovault.dashboard.util.ExcelException;
import com.mvc.cryptovault.dashboard.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import org.apache.commons.io.IOUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;

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
    @PermissionCheck("5")
    public Result<Boolean> newProject(@RequestBody @Valid DProjectDTO dProjectDTO) {
        Boolean result = projectService.newProject(dProjectDTO);
        return new Result<>(result);
    }

    @ApiOperation("项目编辑")
    @PutMapping
    @PermissionCheck("5")
    public Result<Boolean> updateProject(@RequestBody @Valid DProjectDTO dProjectDTO) {
        Boolean result = projectService.updateProject(dProjectDTO);
        return new Result<>(result);
    }

    @ApiOperation("项目删除")
    @DeleteMapping("{id}")
    @PermissionCheck("5")
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
    @PermissionCheck("5")
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

    @ApiOperation("导出众筹参与数据,用于系统之间的交互,json格式")
    @GetMapping("{id}/partake")
    @NotLogin
    public void exportPartake(@PathVariable BigInteger id, HttpServletResponse response) throws Exception {
        List<ExportPartake> list = projectService.exportPartake(id);
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("partake_%s.json", id));
        @Cleanup OutputStream os = response.getOutputStream();
        @Cleanup BufferedOutputStream buff = new BufferedOutputStream(os);
        String jsonStr = JSON.toJSONString(list);
        buff.write(JSON.toJSONBytes(list));
    }

    @ApiOperation("导入众筹参与数据,用于系统之间的交互,json格式")
    @PostMapping("{id}/partake")
    @NotLogin
    public Result<Boolean> importPartake(@RequestBody MultipartFile file, @PathVariable BigInteger id, HttpServletResponse response) throws Exception {
        String fileName = file.getOriginalFilename();
        @Cleanup InputStream in = file.getInputStream();
        String jsonStr = IOUtils.toString(in);
        List<ImportPartake> list = null;
        try {
            list = JSON.parseArray(jsonStr, ImportPartake.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("文件格式错误");
        }
        Assert.isTrue(null != list && list.size() > 0, "文件格式错误");
        Boolean result = projectService.importPartake(id, list, fileName);
        return new Result<>(true);
    }
}
