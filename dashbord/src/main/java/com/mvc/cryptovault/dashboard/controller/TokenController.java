package com.mvc.cryptovault.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.DPairVO;
import com.mvc.cryptovault.common.bean.vo.PairVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DTokenDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenSettingVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenTransSettingVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenVO;
import com.mvc.cryptovault.common.permission.PermissionCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

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
    public Result<List<DTokenVO>> findTokens(@RequestParam(required = false) String tokenName) {
        List<DTokenVO> result = tokenService.findTokens(tokenName);
        return new Result<>(result);
    }

    @ApiOperation("新建币种")
    @PostMapping()
    @PermissionCheck("4")
    public Result<Boolean> newToken(@RequestBody @Valid DTokenDTO dTokenDTO) {
        Boolean result = tokenService.newToken(dTokenDTO);
        return new Result<>(result);
    }

    @ApiOperation("查询币种")
    @GetMapping("{id}")
    public Result<DTokenDTO> getToken(@PathVariable BigInteger id) {
        DTokenDTO result = tokenService.getToken(id);
        return new Result<>(result);
    }

    @ApiOperation("修改币种")
    @PutMapping("{id}")
    @PermissionCheck("4")
    public Result<Boolean> updateToken(@RequestBody @Valid DTokenDTO dTokenDTO) {
        Boolean result = tokenService.updateToken(dTokenDTO);
        return new Result<>(result);
    }

    @ApiOperation("币种参数设置")
    @PutMapping("setting/{id}")
    @PermissionCheck("4")
    public Result<Boolean> tokenSetting(@RequestBody @Valid DTokenSettingVO dto) {
        Boolean result = tokenService.tokenSetting(dto);
        return new Result<>(result);
    }

    @ApiOperation("币种参数查询")
    @GetMapping("setting")
    public Result<PageInfo<DTokenSettingVO>> getTokenSettings(@ModelAttribute @Valid PageDTO pageDTO, @RequestParam(required = false) String tokenName) {
        PageInfo<DTokenSettingVO> result = tokenService.getTokenSettings(tokenName, pageDTO);
        return new Result<>(result);
    }

    @ApiOperation("币种参数详情(目前和列表一样,建议直接使用列表数据)")
    @GetMapping("setting/{id}")
    public Result<DTokenSettingVO> getTokenSetting(@PathVariable BigInteger id) {
        DTokenSettingVO result = tokenService.getTokenSetting(id);
        return new Result<>(result);
    }

    @ApiOperation("币种交易查询")
    @GetMapping("transaction/{id}")
    public Result<DTokenTransSettingVO> getTransSetting(@PathVariable BigInteger id) {
        DTokenTransSettingVO result = tokenService.getTransSetting(id);
        return new Result<>(result);
    }

    @ApiOperation("币种交易设置")
    @PutMapping("transaction")
    @PermissionCheck("4")
    public Result<Boolean> setTransSetting(@RequestBody @Valid DTokenTransSettingVO dto) {
        Boolean result = tokenService.setTransSetting(dto);
        return new Result<>(result);
    }

    @ApiOperation("交易对查询")
    @GetMapping("pair")
    public Result<List<DPairVO>> getPair() {
        List<DPairVO> result = tokenService.getPair();
        return new Result<>(result);
    }

}
