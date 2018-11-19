package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.dto.PairDTO;
import com.mvc.cryptovault.common.bean.vo.OrderInfoVO;
import com.mvc.cryptovault.common.bean.vo.PairVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/14 14:30
 */
@RestController
@RequestMapping("commonPair")
public class CommonPairController extends BaseController {

    @GetMapping("")
    public Result<List<PairVO>> getPair(PairDTO pairDTO) {
        List<PairVO> list = commonTokenService.getPair(pairDTO);
        return new Result<>(list);
    }

    /**
     * @param userId
     * @param pairId
     * @param transactionType 1购买 2出售
     * @return
     */
    @GetMapping("userId/{userId}")
    Result<OrderInfoVO> getInfo(@PathVariable("userId") BigInteger userId, @RequestParam("pairId") BigInteger pairId, @RequestParam Integer transactionType, @RequestParam(required = false) BigInteger id) {
        OrderInfoVO vo = commonTokenService.getInfo(userId, pairId, transactionType, id);
        return new Result<>(vo);
    }

    ;

}
