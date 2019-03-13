package com.mvc.cryptovault.console.controller;

import com.mvc.cryptovault.common.bean.AppUser;
import com.mvc.cryptovault.common.bean.ExplorerBlockSetting;
import com.mvc.cryptovault.common.bean.vo.*;
import com.mvc.cryptovault.console.common.BaseController;
import com.mvc.cryptovault.console.service.AppOrderService;
import com.mvc.cryptovault.console.service.AppUserService;
import com.mvc.cryptovault.console.service.ExplorerBlockInfoService;
import com.mvc.cryptovault.console.service.ExplorerBlockSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2019/3/12 17:56
 */
@RestController
@RequestMapping("explorer")
public class ExplorerController extends BaseController {

    @Autowired
    ExplorerBlockInfoService explorerBlockInfoService;
    @Autowired
    AppUserService appUserService;
    @Autowired
    AppOrderService appOrderService;
    @Autowired
    ExplorerBlockSettingService settingService;

    @PostMapping("setting")
    public Result<Boolean> saveInfo(@RequestBody ExplorerBlockSetting explorerBlockSetting) {
        explorerBlockSetting.setTotalTransaction(null);
        settingService.update(explorerBlockSetting);
        settingService.updateCache(explorerBlockSetting.getId());
        return new Result<>(true);
    }

    @GetMapping("setting")
    public Result<ExplorerBlockSetting> getInfo() {
        ExplorerBlockSetting setting = settingService.findById(BigInteger.ONE);
        setting.setTotalTransaction(null);
        return new Result<>(setting);
    }

    @GetMapping("last")
    public Result<NowBlockVO> getLast() {
        NowBlockVO vo = explorerBlockInfoService.getLastVO();
        return new Result<>(vo);
    }

    @GetMapping("transaction/last")
    public Result<List<ExplorerTransactionSimpleVO>> getLastTransaction(@RequestParam("pageSize") Integer pageSize) {
        List<ExplorerTransactionSimpleVO> result = explorerBlockInfoService.getLastTransaction(pageSize);
        return new Result<>(result);
    }

    @GetMapping()
    public Result<List<ExplorerSimpleVO>> getBlocks(@RequestParam(required = false, value = "blockId") BigInteger blockId, @RequestParam("pageSize") Integer pageSize) {
        List<ExplorerSimpleVO> result = explorerBlockInfoService.getBlocks(blockId, pageSize);
        return new Result<>(result);
    }

    @GetMapping("{blockId}")
    public Result<ExplorerSimpleVO> getBlockInfo(@PathVariable("blockId") BigInteger blockId) {
        ExplorerSimpleVO result = explorerBlockInfoService.getBlockInfo(blockId);
        return new Result<>(result);
    }

    @GetMapping("{blockId}/transactions")
    public Result<List<ExplorerTransactionSimpleVO>> getBlockTransaction(@PathVariable("blockId") BigInteger blockId, @RequestParam(required = false) BigInteger transactionId, @RequestParam("pageSize") Integer pageSize) {
        List<ExplorerTransactionSimpleVO> result = explorerBlockInfoService.getBlockTx(blockId, transactionId, pageSize);
        return new Result<>(result);
    }

    @GetMapping("transaction/tx/{hash}")
    public Result<ExplorerTransactionDetailVO> getTxDetail(@PathVariable("hash") String hash) {
        ExplorerTransactionDetailVO result = explorerBlockInfoService.getDetail(hash);
        return new Result<>(result);
    }

    @GetMapping("address/exist")
    public Result<String> publicKeyExist(@RequestParam("publicKey") String publicKey) {
        AppUser appUser = appUserService.findOneBy("publicKey", publicKey);
        if (null == appUser) {
            return new Result<>("");
        }
        return new Result<>(appUser.getPublicKey());
    }

    @GetMapping("address/balance")
    public Result<List<ExplorerBalanceVO>> getBalance(@RequestParam("publicKey") String publicKey) {
        AppUser appUser = appUserService.findOneBy("publicKey", publicKey);
        if (null == appUser) {
            return null;
        }
        List<ExplorerBalanceVO> result = appUserService.getExplorerBalance(appUser.getId());
        return new Result<>(result);
    }

    @GetMapping("address/order")
    public Result<List<ExplorerSimpleOrder>> getOrders(@RequestParam("publicKey") String publicKey, @RequestParam("pageSize") Integer pageSize, @RequestParam(value = "id", required = false) BigInteger id) {
        AppUser appUser = appUserService.findOneBy("publicKey", publicKey);
        if (null == appUser) {
            return null;
        }
        List<ExplorerSimpleOrder> result = appOrderService.findExplorerOrderList(appUser.getId(), pageSize, id);
        return new Result<>(result);
    }

    @GetMapping("address/order/{id}")
    public Result<ExplorerOrder> getOrderDetail(@PathVariable("id") BigInteger id) {
        ExplorerOrder result = appOrderService.findExplorerOrder(id);
        return new Result<>(result);
    }

}
