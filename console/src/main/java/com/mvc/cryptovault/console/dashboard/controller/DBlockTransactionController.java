package com.mvc.cryptovault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.AdminTransactionDTO;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockStatusDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DBlockeTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DBlockeTransactionVO;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.BlockTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qiyichen
 * @create 2018/11/21 16:41
 */
@RestController
@RequestMapping("dashboard/blockTransaction")
public class DBlockTransactionController extends BaseController {
    @Autowired
    BlockTransactionService blockTransactionService;

    @PutMapping("status")
    public Result<Boolean> updateStatus(@RequestBody DBlockStatusDTO dBlockStatusDTO) {
        blockTransactionService.updateStatus(dBlockStatusDTO);
        return new Result<>(true);
    }

    @GetMapping("")
    public Result<PageInfo<DBlockeTransactionVO>> getTransactions(@ModelAttribute PageDTO pageDTO, @ModelAttribute DBlockeTransactionDTO dBlockeTransactionDTO) {
        PageInfo<DBlockeTransactionVO> result = blockTransactionService.getTransactions(pageDTO, dBlockeTransactionDTO);
        return new Result<>(result);
    }

    @PostMapping("")
    public Result<Boolean> buy(@RequestBody AdminTransactionDTO dto) {
        blockTransactionService.buy(dto);
        return new Result<>(true);
    }

}
