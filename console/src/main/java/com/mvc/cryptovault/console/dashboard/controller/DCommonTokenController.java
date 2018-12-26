package com.mvc.cryptovault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.CommonPair;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DTokenDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenSettingVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenTransSettingVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenVO;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.service.BlockHeightService;
import com.mvc.cryptovault.console.service.CommonPairService;
import com.mvc.cryptovault.console.service.CommonTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qiyichen
 * @create 2018/11/21 16:39
 */
@RestController
@RequestMapping("dashboard/commonToken")
public class DCommonTokenController extends BaseController {
    @Autowired
    BlockHeightService blockHeightService;
    @Autowired
    CommonPairService commonPairService;
    @Autowired
    CommonTokenService commonTokenService;

    @GetMapping("")
    public Result<List<DTokenVO>> findTokens(@RequestParam(value = "tokenName", required = false) String tokenName, @RequestParam(value = "isBlock", required = false) Integer blockType) {
        List<CommonToken> list = null;
        if (StringUtils.isNotBlank(tokenName)) {
            list = commonTokenService.findBy("tokenName", tokenName);
        } else {
            list = commonTokenService.findAll();
        }
        List<DTokenVO> result = new ArrayList<>();
        for (CommonToken token : list) {
            if (null != blockType && BusinessConstant.CLASSIFY_BLOCK.equals(0) && StringUtils.isBlank(token.getTokenType())) {
                //只筛选区块链类型
                continue;
            }
            DTokenVO vo = new DTokenVO();
            vo.setTokenId(token.getId());
            List<CommonPair> pair = commonPairService.findBy("tokenId", token.getId());
            if (null != pair) {
                pair = pair.stream().filter(obj -> obj.getStatus() == 1).collect(Collectors.toList());
            }
            BeanUtils.copyProperties(token, vo);
            Integer tokenInfo = pair.size() == 2 ? 3 : pair.size() == 0 ? 0 : pair.get(0).getBaseTokenId().equals(BigInteger.ONE) ? 1 : 2;
            vo.setPairInfo(tokenInfo);
            result.add(vo);
        }
        return new Result<>(result);
    }

    @PostMapping("")
    public Result<Boolean> newToken(@RequestBody DTokenDTO dTokenDTO) {
        CommonToken token = commonTokenService.findOneBy("tokenName", dTokenDTO.getTokenName());
        Assert.isNull(token, "令牌已存在");
        token = new CommonToken();
        BeanUtils.copyProperties(dTokenDTO, token);
        token.setTokenType(null == dTokenDTO.getBlockType() ? "" : dTokenDTO.getBlockType());
        commonTokenService.save(token);
        commonPairService.insertPair(token.getId(), dTokenDTO.getTokenName());
        commonTokenService.updateAllCache();
        commonTokenService.updateCache(token.getId());
        return new Result<>(true);
    }

    @GetMapping("{id}")
    public Result<DTokenDTO> getToken(@PathVariable BigInteger id) {
        CommonToken token = commonTokenService.findById(id);
        DTokenDTO vo = new DTokenDTO();
        BeanUtils.copyProperties(token, vo);
        vo.setContractAddress(token.getTokenContractAddress());
        vo.setDecimals(token.getTokenDecimal());
        vo.setTokenId(token.getId());
        return new Result<>(vo);
    }

    @PutMapping("")
    public Result<Boolean> updateToken(@RequestBody DTokenDTO dTokenDTO) {
        CommonToken token = new CommonToken();
        BeanUtils.copyProperties(dTokenDTO, token);
        if (dTokenDTO.getTokenId().equals(BusinessConstant.BASE_TOKEN_ID_ETH)) {
            token.setTokenType("ETH");
        } else if (dTokenDTO.getTokenId().equals(BusinessConstant.BASE_TOKEN_ID_USDT)) {
            token.setTokenType("BTC");
        } else {
            token.setTokenType(null == dTokenDTO.getBlockType() ? "" : dTokenDTO.getBlockType());
        }
        token.setId(dTokenDTO.getTokenId());
        token.setTokenContractAddress(dTokenDTO.getContractAddress());
        token.setTokenDecimal(dTokenDTO.getDecimals());
        commonTokenService.update(token);
        commonTokenService.updateAllCache();
        commonTokenService.updateCache(token.getId());
        return new Result<>(true);
    }

    @PutMapping("setting")
    public Result<Boolean> tokenSetting(@RequestBody DTokenSettingVO dto) {
        commonTokenService.tokenSetting(dto);
        commonTokenService.updateAllCache();
        commonTokenService.updateCache(dto.getId());
        return new Result<>(true);
    }

    @GetMapping("setting")
    public Result<PageInfo<DTokenSettingVO>> getTokenSettings(@ModelAttribute @Valid PageDTO pageDTO, @RequestParam(value = "tokenName", required = false) String tokenName) {
        PageInfo<DTokenSettingVO> result = commonTokenService.getTokenSettings(pageDTO, tokenName);
        return new Result<>(result);
    }

    @GetMapping("setting/{id}")
    public Result<DTokenSettingVO> getTokenSetting(@PathVariable("id") BigInteger id) {
        DTokenSettingVO result = commonTokenService.getTokenSetting(id);
        return new Result<>(result);
    }

    @GetMapping("trans/{id}")
    public Result<DTokenTransSettingVO> getTransSetting(@PathVariable("id") BigInteger id) {
        DTokenTransSettingVO result = commonTokenService.getTransSetting(id);
        return new Result<>(result);
    }

    @PutMapping("trans")
    public Result<Boolean> setTransSetting(@RequestBody DTokenTransSettingVO dto) {
        commonTokenService.setTransSetting(dto);
        return new Result<>(true);
    }
}
