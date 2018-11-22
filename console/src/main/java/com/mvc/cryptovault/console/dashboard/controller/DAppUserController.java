package com.mvc.cryptovault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUSerDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserBalanceVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserLogVO;
import com.mvc.cryptovault.console.common.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/21 16:43
 */
@RestController
@RequestMapping("dashboard/appUser")
public class DAppUserController extends BaseController {

    @GetMapping("")
    public Result<PageInfo<DUSerVO>> findUser(@ModelAttribute PageDTO pageDTO, @RequestParam(value = "cellphone", required = false) String cellphone) {
        PageInfo<DUSerVO> result = appUserService.findUser(pageDTO, cellphone);
        return new Result<>(result);
    }

    @GetMapping("{id}")
    public Result<DUSerDetailVO> getUserDetail(@PathVariable("id") BigInteger id) {
        AppUser user = appUserService.findById(id);
        DUSerDetailVO result = new DUSerDetailVO();
        BeanUtils.copyProperties(user, result);
        return new Result<>(result);
    }

    @GetMapping("{id}/balance")
    public Result<List<DUserBalanceVO>> getUserBalance(@PathVariable("id") BigInteger id) {
        List<TokenBalanceVO> list = appUserBalanceService.getAsset(id);
        List<DUserBalanceVO> result = new ArrayList<>(list.size());
        for (TokenBalanceVO vo : list) {
            DUserBalanceVO dUserBalanceVO = new DUserBalanceVO();
            BeanUtils.copyProperties(vo, dUserBalanceVO);
            dUserBalanceVO.setBalance(vo.getValue().multiply(vo.getRatio()));
            result.add(dUserBalanceVO);
        }
        return new Result<>(result);
    }

    @GetMapping("{id}/log")
    public Result<PageInfo<DUserLogVO>> getUserLog(@PathVariable("id") BigInteger id, @ModelAttribute @Valid PageDTO pageDTO) {
        PageInfo<DUserLogVO> result = appUserService.getUserLog(id, pageDTO);
        return new Result<>(result);
    }

}
