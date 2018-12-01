package com.mvc.cryptovault.console.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppProject;
import com.mvc.cryptovault.common.bean.dto.ProjectBuyDTO;
import com.mvc.cryptovault.common.bean.dto.ReservationDTO;
import com.mvc.cryptovault.common.bean.vo.ProjectBuyVO;
import com.mvc.cryptovault.common.bean.vo.PurchaseVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppProjectService;
import com.mvc.cryptovault.console.service.AppProjectUserTransactionService;
import com.mvc.cryptovault.console.service.AppUserBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/13 17:23
 */
@RestController
@RequestMapping("appProjectUserTransaction")
public class AppProjectUserTransactionController extends BaseController {

    @Autowired
    AppProjectUserTransactionService appProjectUserTransactionService;
    @Autowired
    AppProjectService appProjectService;
    @Autowired
    AppUserBalanceService appUserBalanceService;

    @GetMapping()
    Result<PageInfo<PurchaseVO>> getReservation(@RequestParam("userId") BigInteger userId, @ModelAttribute ReservationDTO reservationDTO) {
        List<PurchaseVO> list = appProjectUserTransactionService.getReservation(userId, reservationDTO);
        return new Result<>(new PageInfo<>(list));
    }

    @GetMapping("chaseInfo")
    Result<ProjectBuyVO> getPurchaseInfo(@RequestParam("userId") BigInteger userId, @RequestParam("projectId") BigInteger projectId) {
        AppProject project = appProjectService.findById(projectId);
        ProjectBuyVO vo = appUserBalanceService.getBalance(userId, project);
        return new Result<>(vo);
    }

    /**
     * TODO 操作耗时则修改为队列
     *
     * @param userId
     * @param projectId
     * @param dto
     * @return
     */
    @PostMapping("buy")
    Result<Boolean> buy(@RequestParam("userId") BigInteger userId, @RequestParam("projectId") BigInteger projectId, @RequestBody ProjectBuyDTO dto) {
        Boolean result = appProjectUserTransactionService.buy(userId, projectId, dto);
        return new Result<>(result);
    }

    ;
}
