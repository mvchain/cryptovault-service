package com.mvc.cryptovault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppUserTransaction;
import com.mvc.cryptovault.common.bean.ExportOrders;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.OverTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTransactionVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.OverTransactionVO;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppUserTransactionService;
import com.mvc.cryptovault.console.service.CommonAddressService;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/21 16:44
 */
@RestController
@RequestMapping("dashboard/appUserTransaction")
public class DAppUserTransactionController extends BaseController {
    @Autowired
    AppUserTransactionService appUserTransactionService;
    @Autowired
    CommonAddressService commonAddressService;

    @GetMapping("")
    public Result<PageInfo<DTransactionVO>> findTransaction(@ModelAttribute PageDTO pageDTO, @ModelAttribute DTransactionDTO dTransactionDTO) {
        PageInfo<DTransactionVO> result = appUserTransactionService.findTransaction(pageDTO, dTransactionDTO);
        return new Result<>(result);
    }

    @DeleteMapping("{id}")
    public Result<Boolean> cancel(@PathVariable("id") BigInteger id) {
        AppUserTransaction userTransaction = appUserTransactionService.findById(id);
        Assert.isTrue(userTransaction.getStatus() == 0, "无法取消");
        userTransaction.setStatus(4);
        appUserTransactionService.update(userTransaction);
        appUserTransactionService.updateAllCache();
        appUserTransactionService.updateCache(id);
        return new Result<>(true);
    }

    @GetMapping("over")
    public Result<PageInfo<OverTransactionVO>> overList(@ModelAttribute PageDTO pageDTO, @ModelAttribute OverTransactionDTO overTransactionDTO) {
        PageInfo<OverTransactionVO> result = appUserTransactionService.overList(pageDTO, overTransactionDTO);
        return new Result<>(result);
    }

    @GetMapping("collect")
    public Result<List<ExportOrders>> exportCollect() throws IOException, BitcoindException, CommunicationException {
        List<ExportOrders> result = commonAddressService.exportCollect();
        return new Result<>(result);
    }
    @GetMapping("sign")
    public Result<List<ExportOrders>> exportSign() throws IOException, BitcoindException, CommunicationException {
        List<ExportOrders> result = commonAddressService.exportSign();
        return new Result<>(result);
    }

}
