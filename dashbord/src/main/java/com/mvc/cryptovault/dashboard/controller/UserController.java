package com.mvc.cryptovault.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.DebitRechargeDTO;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUSerDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserBalanceVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserLogVO;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户相关")
public class UserController extends BaseController {

    private static LinkedHashMap<String, String> userLogMap = new LinkedHashMap<>();

    @ApiOperation("用户列表查询")
    @GetMapping
    public Result<PageInfo<DUSerVO>> findUser(@ModelAttribute @Valid PageDTO pageDTO, @RequestParam(required = false) String cellphone, @RequestParam(required = false) Integer status) {
        PageInfo<DUSerVO> result = userService.findUser(pageDTO, cellphone, status);
        return new Result<>(result);
    }

    @ApiOperation("用户详情获取")
    @GetMapping("{id}")
    public Result<DUSerDetailVO> getUserDetail(@PathVariable BigInteger id) {
        DUSerDetailVO result = userService.getUserDetail(id);
        return new Result<>(result);
    }

    @ApiOperation("用户资产获取,不分页")
    @GetMapping("{id}/balance")
    public Result<List<DUserBalanceVO>> getBalance(@PathVariable BigInteger id) {
        List<DUserBalanceVO> result = userService.getBalance(id);
        return new Result<>(result);
    }

    @ApiOperation("用户操作记录查询")
    @GetMapping("{id}/log")
    public Result<PageInfo<DUserLogVO>> getUserLog(@ModelAttribute @Valid PageDTO pageDTO, @PathVariable BigInteger id) {
        PageInfo<DUserLogVO> result = userService.getUserLog(id, pageDTO);
        return new Result<>(result);
    }

    @ApiOperation("用户操作记录导出")
    @NotLogin
    @GetMapping("{id}/log/excel")
    public void userLogExport(HttpServletResponse response, @RequestParam String sign, @ModelAttribute @Valid PageDTO pageDTO, @PathVariable BigInteger id) throws IOException, ExcelException {
        getUserIdBySign(sign);
        PageInfo<DUserLogVO> result = userService.getUserLog(id, pageDTO);
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.addHeader("Content-Disposition", "attachment; filename=" + String.format("userlog_%s.xls", System.currentTimeMillis()));
        @Cleanup OutputStream os = response.getOutputStream();
        ExcelUtil.listToExcel(result.getList(), getUserLogMap(), "UserLogTable", os);
    }

    private LinkedHashMap<String, String> getUserLogMap() {
        if (userLogMap.isEmpty()) {
            userLogMap.put("createdAt", "时间");
            userLogMap.put("message", "操作描述");
        }
        return userLogMap;
    }

    @ApiOperation("修改用户状态0禁用 1启用")
    @PutMapping("{id}/status")
    @PermissionCheck("3")
    public Result<Boolean> updateStatus(@PathVariable BigInteger id, @RequestParam Integer status) {
        userService.updateStatus(id, status);
        return new Result<>(true);
    }

    @ApiOperation("导入用户,数据为json格式,以用户id区分,不存在则新建,存在则根据修改时间修改,导入时vp方记录时间戳,下次导入尽量使用增量的方式进行更新")
    @PostMapping
    public Result<Boolean> importAppUser(@RequestBody MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        @Cleanup InputStream in = file.getInputStream();
        String jsonStr = IOUtils.toString(in);
        List<AppUser> list = null;
        try {
            list = JSON.parseArray(jsonStr, AppUser.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("文件格式错误");
        }
        Assert.isTrue(null != list && list.size() > 0, "文件格式错误");
        Boolean result = userService.importAppUser(list, fileName);
        return new Result<>(result);
    }

    @ApiOperation("导入余额（划账）")
    @PutMapping
    public Result<Boolean> debitRecharge(@RequestBody DebitRechargeDTO rechargeDTO) {
        userService.debitRecharge(rechargeDTO);
        return new Result<>(true);
    }

}
