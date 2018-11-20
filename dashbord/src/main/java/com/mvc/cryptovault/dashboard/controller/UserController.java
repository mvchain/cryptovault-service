package com.mvc.cryptovault.dashboard.controller;

import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUSerDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserBalanceVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserLogVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@RestController
@RequestMapping("user")
@Api(tags = "用户相关")
public class UserController extends BaseController {

    @ApiOperation("用户列表查询")
    @GetMapping
    public Result<DUSerVO> findUser(@ModelAttribute @Valid PageDTO pageDTO, @RequestParam String cellphone) {
        return null;
    }

    @ApiOperation("用户详情获取")
    @GetMapping("{id}")
    public Result<DUSerDetailVO> getUserDetail(@PathVariable BigDecimal id) {
        return null;
    }

    @ApiOperation("用户资产获取,不分页")
    @GetMapping("{id}/balance")
    public Result<List<DUserBalanceVO>> getBalance(@PathVariable BigDecimal id) {
        return null;
    }


    @ApiOperation("用户操作记录查询")
    @GetMapping("{id}/log")
    public Result<DUserLogVO> getUserLog(@PathVariable BigInteger id) {
        return null;
    }


    @ApiOperation("用户操作记录导出")
    @GetMapping("{id}/log/excel")
    public void userLogExport(@RequestParam String sign, @PathVariable BigInteger id) {
        return;
    }

}
