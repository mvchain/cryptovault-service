package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.FinancialSimpleVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("理财产品列表")
    @GetMapping
    public Result<List<FinancialSimpleVO>> getList(@ModelAttribute PageDTO pageDTO, @RequestParam(required = false) BigInteger id) {

        return null;
    }


    @ApiOperation("持仓列表")
    public


    @ApiOperation("理财资产统计")

    @ApiOperation("理财详情查看(购买时详情获取)")

    @ApiOperation("持仓详情")

    @ApiOperation("持仓收益列表")

    @ApiOperation("参与持仓")

    @ApiOperation("取消持仓")
}
