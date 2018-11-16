package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.vo.KLineVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/14 14:35
 */
@RestController
@RequestMapping("appKline")
public class AppKlineController extends BaseController {

    @GetMapping()
    public Result<KLineVO> getTransactions(@RequestParam("pairId") BigInteger pairId) {
        KLineVO vo = appKlineService.getKLine(pairId);
        return new Result<>(vo);
    }

}
