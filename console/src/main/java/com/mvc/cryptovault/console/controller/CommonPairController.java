package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.vo.OrderInfoVO;
import com.mvc.cryptovault.common.bean.vo.PairVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/14 14:30
 */
@RequestMapping("commonPair")
public class CommonPairController extends BaseController {

    @GetMapping("")
    public Result<List<PairVO>> getPair(@RequestParam("pairType") Integer pairType) {
        List<PairVO> list = commonTokenService.getPair(pairType);
        return new Result<>(list);
    }

    /**
     * @param userId
     * @param pairId
     * @param transactionType 1购买 2出售
     * @return
     */
    @GetMapping("userId/{userId}")
    Result<OrderInfoVO> getInfo(@PathVariable("userId") BigInteger userId, @RequestParam("pairId") BigInteger pairId, @RequestParam Integer transactionType) {
        OrderInfoVO vo = commonTokenService.getInfo(userId, pairId, transactionType);
        return new Result<>(vo);
    }

    ;

}