package com.mvc.cryptovault.console.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TransactionTokenVO;
import com.mvc.cryptovault.console.common.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author qiyichen
 * @create 2018/11/13 11:44
 */
@RestController
@RequestMapping("commonToken")
public class CommonTokenController extends BaseController {

    @GetMapping()
    public Result<PageInfo<CommonToken>> getTokenList(@ModelAttribute CommonToken commonToken, @ModelAttribute PageDTO pageDTO) {
        List<CommonToken> result = commonTokenService.findAll();
        Stream<CommonToken> stream = result.stream();
        if (null != commonToken.getVisible()) {
            stream = stream.filter(obj -> obj.getVisible().equals(commonToken.getVisible()));
        }
        if (StringUtils.isNotBlank(commonToken.getTokenName())) {
            stream = stream.filter(obj -> obj.getTokenName().equalsIgnoreCase(commonToken.getTokenName()));
        }
        if (null != pageDTO.getUpdatedStartAt()) {
            stream = stream.filter(obj -> obj.getUpdatedAt().compareTo(commonToken.getUpdatedAt()) > 0);
        }
        result = stream.collect(Collectors.toList());
        return new Result<>(new PageInfo<>(result));
    }

    @GetMapping("transactionInfo")
    public Result<TransactionTokenVO> getTransactionInfo(@RequestParam BigInteger userId, @RequestParam("tokenId") BigInteger tokenId) {
        CommonToken token = commonTokenService.findById(tokenId);
        TransactionTokenVO vo = new TransactionTokenVO();
        vo.setBalance(appUserBalanceService.getBalanceByTokenId(userId, tokenId));
        vo.setFee(token.getTransaferFee());
        vo.setFeeTokenName(token.getTokenType());
        return new Result<>(vo);
    }

}
