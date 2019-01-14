package com.mvc.cryptovault.app.controller;

import com.mvc.cryptovault.app.service.TokenService;
import com.mvc.cryptovault.common.bean.vo.ExchangeRateVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TokenDetailVO;
import com.mvc.cryptovault.common.bean.vo.TokenRatioVO;
import com.mvc.cryptovault.common.swaggermock.SwaggerMock;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

/**
 * 令牌相关
 *
 * @author qiyichen
 * @create 2018/11/7 11:02
 */

@Api(tags = "令牌相关")
@RequestMapping("token")
@RestController
public class TokenController extends BaseController {

    @Autowired
    TokenService tokenService;

    @ApiOperation("获取币种列表,需要传入时间戳,必须缓存.添加移除时本地记录并保存顺序.如果返回内容为空则代表无变化,否则需要刷新本地数据库(全量刷新).搜索时本地搜索")
    @GetMapping
    @SwaggerMock("${token.all}")
    public Result<List<TokenDetailVO>> getTokens(@RequestParam(required = false) BigInteger timestamp) throws Exception {
        return new Result<>(tokenService.getTokens(timestamp));
    }

    @ApiOperation("获取币种比值,用于计算资产总值.以CNY为基础货币.建议缓存")
    @GetMapping("base")
    @SwaggerMock("${token.base}")
    public Result<List<TokenRatioVO>> getBase() {
        return new Result<>(tokenService.getBase());
    }

    @ApiOperation("获取汇率，每12小时刷新.客户端控制调用频率")
    @GetMapping("exchange/rate")
    public Result<List<ExchangeRateVO>> getExchangeRate() {
        return new Result<>(tokenService.getExchangeRate());
    }

}
