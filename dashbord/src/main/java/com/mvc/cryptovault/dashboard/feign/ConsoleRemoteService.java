package com.mvc.cryptovault.dashboard.feign;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.AdminUser;
import com.mvc.cryptovault.common.bean.BlockSign;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.ExportOrders;
import com.mvc.cryptovault.common.bean.dto.AdminTransactionDTO;
import com.mvc.cryptovault.common.bean.dto.ImportPartake;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.AdminWalletVO;
import com.mvc.cryptovault.common.bean.vo.DPairVO;
import com.mvc.cryptovault.common.bean.vo.ExportPartake;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.*;
import com.mvc.cryptovault.common.dashboard.bean.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@FeignClient("console")
public interface ConsoleRemoteService {

    @GetMapping("dashboard/adminUser")
    Result<PageInfo<AdminVO>> getAdmins(@RequestParam("userId") BigInteger userId, @RequestBody PageDTO dto);

    @GetMapping("dashboard/adminUser/{id}")
    Result<AdminDetailVO> getAdminDetail(@PathVariable("id") BigInteger id);

    @DeleteMapping("dashboard/adminUser/{id}")
    Result<Boolean> deleteAdmin(@RequestParam(value = "userId", required = false) BigInteger userId, @PathVariable("id") BigInteger id);

    @GetMapping("dashboard/adminUser/password")
    Result<Boolean> updatePwd(@RequestParam(value = "userId", required = false) BigInteger userId, @RequestBody AdminPasswordDTO adminPasswordDTO);

    @GetMapping("dashboard/adminUser/username")
    Result<AdminUser> getAdminByUsername(@RequestParam(value = "username", required = false) String username);

    @PostMapping("dashboard/adminUser")
    Result<Boolean> newAdmin(@RequestBody AdminDTO adminDTO);

    @PutMapping("dashboard/adminUser")
    Result<Boolean> updateAdmin(@RequestParam("userId") BigInteger userId, @RequestBody AdminDTO adminDTO);

    @GetMapping("dashboard/adminUser/balance")
    Result<BigDecimal> getBalance(@RequestParam(value = "tokenId", required = false) BigInteger tokenId);

    @GetMapping("dashboard/commonToken/hold")
    Result<List<DHoldVO>> getHold();

    @PutMapping("dashboard/commonToken/hold")
    Result<Boolean> setHold(@RequestBody List<DHoldVO> list);

    @GetMapping("dashboard/commonToken/fee")
    Result<List<DHoldVO>> getFee();

    @PutMapping("dashboard/commonToken/fee")
    Result<Boolean> setFee(@RequestBody List<DHoldVO> list);

    @GetMapping("dashboard/commonToken")
    Result<List<DTokenVO>> findTokens(@RequestParam(value = "tokenName", required = false) String tokenName);

    @PostMapping("dashboard/commonToken")
    Result<Boolean> newToken(@RequestBody DTokenDTO dTokenDTO);

    @PutMapping("dashboard/commonToken")
    Result<Boolean> updateToken(@RequestBody DTokenDTO dTokenDTO);

    @GetMapping("dashboard/commonToken/{id}")
    Result<DTokenDTO> getToken(@PathVariable("id") BigInteger id);

    @PutMapping("dashboard/commonToken/setting")
    Result<Boolean> tokenSetting(@RequestBody DTokenSettingVO dto);

    @GetMapping("dashboard/commonToken/setting")
    Result<PageInfo<DTokenSettingVO>> getTokenSettings(@RequestBody PageDTO pageDTO, @RequestParam(value = "tokenName", required = false) String tokenName);

    @GetMapping("dashboard/commonToken/setting/{id}")
    Result<DTokenSettingVO> getTokenSetting(@PathVariable("id") BigInteger id);

    @GetMapping("dashboard/commonToken/trans/{id}")
    Result<DTokenTransSettingVO> getTransSetting(@PathVariable("id") BigInteger id);

    @PutMapping("dashboard/commonToken/trans")
    Result<Boolean> setTransSetting(@RequestBody DTokenTransSettingVO dto);

    @PutMapping("dashboard/blockTransaction/status")
    Result<Boolean> updateStatus(@RequestBody DBlockStatusDTO dBlockStatusDTO);

    @GetMapping("dashboard/blockTransaction")
    Result<PageInfo<DBlockeTransactionVO>> getTransactions(@RequestBody DBlockeTransactionDTO dBlockeTransactionDTO);

    @GetMapping("dashboard/commonAddress/count")
    Result<Integer> accountCount(@RequestParam("tokenType") String tokenType);

    @GetMapping("dashboard/appUser")
    Result<PageInfo<DUSerVO>> findUser(@RequestBody PageDTO pageDTO, @RequestParam(value = "cellphone", required = false) String cellphone);

    @GetMapping("dashboard/appUser/{id}")
    Result<DUSerDetailVO> getUserDetail(@PathVariable("id") BigInteger id);

    @GetMapping("dashboard/appUser/{id}/balance")
    Result<List<DUserBalanceVO>> getUserBalance(@PathVariable("id") BigInteger id);

    @GetMapping("dashboard/appUser/{id}/log")
    Result<PageInfo<DUserLogVO>> getUserLog(@RequestBody PageDTO pageDTO, @PathVariable("id") BigInteger id);

    @GetMapping("dashboard/appUserTransaction")
    Result<PageInfo<DTransactionVO>> findTransaction(@RequestBody DTransactionDTO dTransactionDTO);

    @DeleteMapping("dashboard/appUserTransaction/{id}")
    Result<Boolean> cancel(@PathVariable("id") BigInteger id);

    @GetMapping("dashboard/appUserTransaction/collect")
    Result<List<ExportOrders>> exportCollect();

    @GetMapping("dashboard/appUserTransaction/sign")
    Result<List<ExportOrders>> exportSign();

    @GetMapping("dashboard/appUserTransaction/over")
    Result<PageInfo<OverTransactionVO>> overList(@RequestBody OverTransactionDTO overTransactionDTO);

    @PutMapping("dashboard/appProjectUserTransaction/{id}")
    Result<Boolean> cancelProject(@PathVariable("id") BigInteger id);

    @GetMapping("dashboard/appProjectUserTransaction")
    Result<PageInfo<DProjectOrderVO>> findOrders(@RequestBody DProjectOrderDTO dto);

    @DeleteMapping("dashboard/appProject/{id}")
    Result<Boolean> deleteProject(@PathVariable("id") BigInteger id);

    @PutMapping("dashboard/appProject")
    Result<Boolean> updateProject(@RequestBody DProjectDTO dProjectDTO);

    @PostMapping("dashboard/appProject")
    Result<Boolean> newProject(@RequestBody DProjectDTO dProjectDTO);

    @GetMapping("dashboard/appProject/{id}")
    Result<DProjectDetailVO> getDetail(@PathVariable("id") BigInteger id);

    @GetMapping("dashboard/appProject")
    Result<PageInfo<DProjectVO>> projects(@RequestBody PageDTO pageDTO);

    @GetMapping("dashboard/commonPair")
    Result<List<DPairVO>> getPair();

    @PostMapping("dashboard/commonAddress")
    Result<Boolean> importAddress(@RequestBody List<CommonAddress> list, @RequestParam("fileName") String fileName);

    @PostMapping("dashboard/blockSign")
    Result<Boolean> importSign(@RequestBody List<BlockSign> list, @RequestParam("fileName") String fileName);

    @PutMapping("dashboard/appUser/{id}/status")
    Result<Boolean> updateUserStatus(@PathVariable("id") BigInteger id, @RequestParam("status") Integer status);

    @GetMapping("dashboard/appProject/{id}/partake")
    Result<List<ExportPartake>> exportPartake(@PathVariable("id") BigInteger id);

    @PostMapping("dashboard/appProject/{id}/partake")
    Result<Boolean> importPartake(@PathVariable("id") BigInteger id, @RequestBody List<ImportPartake> list, @RequestParam("fileName") String fileName);

    @GetMapping("dashboard/adminUser/wallet")
    Result<AdminWalletVO> getAdminWallet();

    @PostMapping("dashboard/blockTransaction")
    Result<Boolean> buy(@RequestBody AdminTransactionDTO dto);
}
