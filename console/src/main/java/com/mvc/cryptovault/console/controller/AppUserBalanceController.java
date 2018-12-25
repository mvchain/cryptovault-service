package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.AssertVisibleDTO;
import com.mvc.cryptovault.common.bean.dto.DebitDTO;
import com.mvc.cryptovault.common.bean.dto.DebitRechargeDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.common.util.MessageConstants;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.service.AppUserBalanceService;
import com.mvc.cryptovault.console.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/14 14:38
 */
@RestController
@RequestMapping("appUserBalance")
public class AppUserBalanceController extends BaseController {

    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    AppUserService appUserService;

    @GetMapping("{userId}")
    public Result<List<TokenBalanceVO>> getAsset(@PathVariable("userId") BigInteger userId) {
        List<TokenBalanceVO> list = appUserBalanceService.getAsset(userId, false);
        return new Result<>(list);
    }

    @PutMapping("{userId}")
    public Result<Boolean> setAssetVisible(@RequestBody @Valid AssertVisibleDTO visibleDTO, @PathVariable("userId") BigInteger userId) {
        appUserBalanceService.setAssetVisible(visibleDTO, userId);
        return new Result<>(true);
    }

    @GetMapping("sum/{userId}")
    public Result<BigDecimal> getBalance(@PathVariable("userId") BigInteger userId) {
        //TODO 修改统计方式为缓存方式
        List<TokenBalanceVO> list = appUserBalanceService.getAsset(userId, true);
        BigDecimal sum = list.stream().map(obj -> obj.getRatio().multiply(obj.getValue())).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Result<>(sum);
    }

    @GetMapping("debit/{userId}")
    public Result<BigDecimal> debit(@PathVariable("userId") BigInteger userId) {
        BigDecimal result = appUserBalanceService.getBalanceByTokenId(userId, BusinessConstant.BASE_TOKEN_ID_BALANCE);
        return new Result<>(result);
    }

    @PostMapping("debit/{userId}")
    public Result<Boolean> debit(@PathVariable("userId") BigInteger userId, @RequestBody DebitDTO debitDTO) {
        AppUser user = appUserService.findById(userId);
        Assert.isTrue(user.getTransactionPassword().equalsIgnoreCase(debitDTO.getPassword()), MessageConstants.getMsg("USER_TRANS_PASS_WRONG"));
        appUserBalanceService.debit(userId, BigDecimal.ZERO.subtract(debitDTO.getValue()), 2, 0);
        return new Result<>(true);
    }

    @PutMapping("debit")
    public Result<Boolean> debitRecharge(@RequestBody DebitRechargeDTO debitDTO) {
        AppUser user = appUserService.findOneBy("cellphone", debitDTO.getCellphone());
        Assert.notNull(user, "用户不存在");
        appUserBalanceService.debit(user.getId(), debitDTO.getValue(), 1, 1);
        return new Result<>(true);
    }

}
