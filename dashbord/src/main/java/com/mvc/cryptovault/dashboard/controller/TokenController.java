package com.mvc.cryptovault.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DTokenDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenSettingVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenTransSettingVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.PairDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/19 19:48
 */
@RestController
@RequestMapping("token")
@Api(tags = "令牌管理相关")
public class TokenController extends BaseController {

    @ApiOperation("币种列表查询,结果不分页")
    @GetMapping
    public Result<DTokenVO> findTokens(@RequestParam(required = false) String tokenName) {
        return null;
    }

    @ApiOperation("新建币种")
    @PostMapping()
    public Result<Boolean> newToken(@RequestBody @Valid DTokenDTO dTokenDTO) {
        return null;
    }

    @ApiOperation("修改币种")
    @PutMapping("{id}")
    public Result<Boolean> updateToken(@RequestBody @Valid DTokenDTO dTokenDTO) {
        return null;
    }

    @ApiOperation("币种参数设置")
    @PutMapping("setting/{id}")
    public Result<Boolean> tokenSetting(@RequestBody @Valid DTokenSettingVO dto) {
        return null;
    }


    @ApiOperation("币种参数查询")
    @GetMapping("setting")
    public Result<PageInfo<DTokenSettingVO>> getTokenSettings(@RequestParam String tokenName) {
        return null;
    }

    @ApiOperation("币种参数详情(目前和列表一样,建议直接使用列表数据)")
    @GetMapping("setting/{id}")
    public Result<DTokenSettingVO> getTokenSetting(@PathVariable BigInteger id) {
        return null;
    }

    @ApiOperation("币种交易查询")
    @GetMapping("transaction/{id}")
    public Result<DTokenTransSettingVO> getTransSetting(@PathVariable BigInteger id) {
        return null;
    }

    @ApiOperation("币种交易设置")
    @PutMapping("transaction")
    public Result<Boolean> setTransSetting(@RequestBody @Valid DTokenTransSettingVO dto) {
        return null;
    }

    @ApiOperation("交易对删除")
    @DeleteMapping("pair/{pairId}")
    public Result<Boolean> deletePair(@PathVariable BigInteger pairId) {
        return null;
    }

    @ApiOperation("交易对详细信息查询,1VRT 余额")
    @GetMapping("pair")
    public Result<PairDetailVO> getPairDetail(@RequestParam BigInteger tokenId) {
        return null;
    }

    @ApiOperation("交易对设置")
    @PutMapping("pair")
    public Result<PairDetailVO> setPair(@RequestBody @Valid PairDetailVO vo) {
        return null;
    }
}
