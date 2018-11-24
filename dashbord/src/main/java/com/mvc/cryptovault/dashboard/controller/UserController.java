package com.mvc.cryptovault.dashboard.controller;

import com.github.pagehelper.PageInfo;
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
    public Result<PageInfo<DUSerVO>> findUser(@ModelAttribute @Valid PageDTO pageDTO, @RequestParam(required = false) String cellphone) {
        PageInfo<DUSerVO> result = userService.findUser(pageDTO, cellphone);
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
    @GetMapping("{id}/log/excel")
    public void userLogExport(@RequestParam String sign, @PathVariable BigInteger id) {
        return;
    }

}
