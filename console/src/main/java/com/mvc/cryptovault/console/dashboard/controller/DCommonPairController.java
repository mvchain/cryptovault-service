package com.mvc.cryptovault.console.dashboard.controller;

import com.mvc.cryptovault.common.bean.CommonPair;
import com.mvc.cryptovault.common.bean.vo.DPairVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/28 15:02
 */
@RestController
@RequestMapping("dashboard/commonPair")
public class DCommonPairController extends BaseController {

    @GetMapping
    public Result<List<DPairVO>> getPair() {
        List<CommonPair> pairs = commonPairService.findAll();
        List<DPairVO> result = new ArrayList<>(pairs.size());
        for (CommonPair pair : pairs) {
            DPairVO vo = new DPairVO();
            vo.setPairId(pair.getId());
            vo.setPairName(pair.getPairName());
            result.add(vo);
        }
        return new Result<>(result);
    }

    ;
}
