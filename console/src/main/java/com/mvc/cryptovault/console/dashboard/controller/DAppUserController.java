package com.mvc.cryptovault.console.dashboard.controller;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.dashboard.bean.dto.DUSerVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUSerDetailVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserBalanceVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DUserLogVO;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppUserBalanceService;
import com.mvc.cryptovault.console.service.AppUserService;
import com.mvc.cryptovault.console.service.BlockSignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
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
    @Autowired
    AppUserService appUserService;
    @Autowired
    AppUserBalanceService appUserBalanceService;
    @Autowired
    BlockSignService blockSignService;

    @GetMapping("")
    public Result<PageInfo<DUSerVO>> findUser(@ModelAttribute PageDTO pageDTO, @RequestParam(value = "cellphone", required = false) String cellphone, @RequestParam(value = "status", required = false) Integer status) {
        PageInfo<DUSerVO> result = appUserService.findUser(pageDTO, cellphone, status);
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
        List<TokenBalanceVO> list = appUserBalanceService.getAsset(id, true);
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

    @PutMapping("{id}/status")
    public Result<Boolean> updateStatus(@PathVariable BigInteger id, @RequestParam Integer status) {
        Assert.isTrue(status == 1 || status == 0, "状态错误");
        AppUser appUser = new AppUser();
        appUser.setId(id);
        appUser.setUpdatedAt(System.currentTimeMillis());
        appUser.setStatus(status);
        appUserService.update(appUser);
        appUserService.updateCache(id);
        return new Result<>(true);
    }

    @PostMapping()
    public Result<Boolean> importAppUser(@RequestBody List<AppUser> list, @RequestParam("fileName") String fileName) {
        String obj = redisTemplate.boundValueOps(RedisConstant.USER_IMPORT + fileName).get();
        if (null != obj) {
            throw new IllegalArgumentException("该文件正在导入,请稍后");
        }
        blockSignService.importAppUser(list, fileName);
        return new Result<>(true);
    }
}
