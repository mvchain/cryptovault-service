package com.mvc.cryptovault.app.feign;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.app.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.bean.dto.*;
import com.mvc.cryptovault.common.bean.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@FeignClient("console")
public interface ConsoleRemoteService {

    @GetMapping("appUserBalance/{userId}")
    Result<List<TokenBalanceVO>> getAsset(@PathVariable("userId") BigInteger userId);

    @PutMapping("appUserBalance/{userId}")
    Result<Boolean> updateVisible(@PathVariable("userId") BigInteger userId, @RequestBody AssertVisibleDTO assertVisibleDTO);

    @GetMapping("appUserBalance/sum/{userId}")
    Result<BigDecimal> getBalance(@PathVariable("userId") BigInteger userId);

    @GetMapping("appOrder/user")
    Result<List<TransactionSimpleVO>> getTransactions(
            @RequestParam("userId") BigInteger userId,
            @RequestParam("transactionType") Integer transactionType,
            @RequestParam("id") BigInteger id,
            @RequestParam("type") Integer type,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam("tokenId") BigInteger tokenId,
            @RequestParam("classify") Integer classify
    );

    @GetMapping("appOrder/{id}")
    Result<TransactionDetailVO> getTransaction(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id);

    @GetMapping("appUserAddress/{userId}")
    Result<String> getAddress(@PathVariable("userId") BigInteger userId, @RequestParam("tokenId") BigInteger tokenId);

    @GetMapping("appUserBalance/debit/{userId}")
    Result<BigDecimal> debit(@PathVariable("userId") BigInteger userId);

    @PostMapping("appUserBalance/debit/{userId}")
    Result<Boolean> debit(@PathVariable("userId") BigInteger userId, @RequestBody DebitDTO debitDTO);

    @GetMapping("commonToken/transactionInfo")
    Result<TransactionTokenVO> getTransactionInfo(@RequestParam("userId") BigInteger userId, @RequestParam("tokenId") BigInteger tokenId);

    @PostMapping("blockTransaction/{userId}")
    Result<Boolean> sendTransaction(@PathVariable("userId") BigInteger userId, @RequestBody TransactionDTO transactionDTO);

    @GetMapping("appMessage")
    Result<PageInfo<AppMessage>> getlist(@RequestParam("userId") BigInteger userId, @RequestParam("timestamp") BigInteger timestamp, @RequestParam("type") Integer type, @RequestParam("pageSize") Integer pageSize);

    @PutMapping("appMessage/{id}")
    Result<Boolean> read(@RequestParam("userId") BigInteger userId, @PathVariable("id") BigInteger id);

    @GetMapping("appProject")
    Result<PageInfo<AppProject>> getProject(@RequestParam("userId") BigInteger userId,@RequestParam("projectType") Integer projectType, @RequestParam("id") BigInteger projectId, @RequestParam("type") Integer type, @RequestParam("pageSize") Integer pageSize);

    @GetMapping("appProject/{id}")
    Result<AppProject> getProjectById(@PathVariable("id") BigInteger id);

    @GetMapping("appProjectUserTransaction")
    Result<PageInfo<PurchaseVO>> getReservation(@RequestParam("userId") BigInteger userId, @RequestBody ReservationDTO reservationDTO);

    @GetMapping("appProjectUserTransaction/chaseInfo")
    Result<ProjectBuyVO> getPurchaseInfo(@RequestParam("userId") BigInteger userId, @RequestParam("projectId") BigInteger projectId);

    @PostMapping("appProjectUserTransaction/buy")
    Result<Boolean> buy(@RequestParam("userId") BigInteger userId, @RequestParam("projectId") BigInteger id, @RequestBody ProjectBuyDTO projectBuyDTO);

    @GetMapping("commonToken")
    Result<PageInfo<CommonToken>> all(@RequestParam("visiable") Integer visiable, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("updatedStartAt") BigInteger timestamp);

    @GetMapping("commonTokenPrice")
    Result<PageInfo<CommonTokenPrice>> price();

    @GetMapping("commonPair")
    Result<List<PairVO>> getPair(@RequestBody PairDTO pairDTO);

    @GetMapping("appUserTransaction")
    Result<List<OrderVO>> getTransactions(@RequestBody OrderDTO orderDTO);

    @GetMapping("appKline")
    Result<KLineVO> getTransactions(@RequestParam("pairId") BigInteger pairId);

    @GetMapping("appKline/tickers")
    Result<TickerVO> getTickers(@RequestParam("pairId") BigInteger pairId);

    @GetMapping("appUserTransaction/userId/{userId}")
    Result<List<MyOrderVO>> getUserTransactions(@PathVariable("userId") BigInteger userId, @RequestBody MyTransactionDTO myTransactionDTO);

    @PostMapping("appUserTransaction/userId/{userId}")
    Result<Boolean> buy(@PathVariable("userId") BigInteger userId, @RequestBody TransactionBuyDTO dto);

    @GetMapping("commonPair/userId/{userId}")
    Result<OrderInfoVO> getInfo(@PathVariable("userId") BigInteger userId, @RequestParam("pairId") BigInteger pairId, @RequestParam("transactionType") Integer transactionType);

    @PutMapping("appUserTransaction/userId/{userId}")
    Result<Boolean> cancel(@PathVariable("userId") BigInteger userId, @RequestParam("id") BigInteger id);

    @GetMapping("user/{id}")
    Result<AppUser> getUserById(@PathVariable("id") BigInteger userId);

    @GetMapping("user/username")
    Result<AppUser> getUserByUsername(@RequestParam("username") String username);

    @GetMapping("user/{id}/tag")
    Result<String> getTag(@PathVariable("id") BigInteger userId);

    @PostMapping("user")
    Result<AppUserRetVO> register(@RequestBody AppUserDTO appUserDTO);

    @PostMapping("user/mnemonics")
    Result<Boolean> mnemonicsActive(@RequestParam("email") String email);

    @PostMapping("user/password")
    Result<Boolean> forget(@RequestParam("userId") BigInteger userId, @RequestParam("password") String password, @RequestParam("type") Integer type);

    @GetMapping("user/pvKey")
    Result<AppUser> getUserByPvKey(@RequestParam("value") String value);

    @PutMapping("user")
    Result<Boolean> updateUser(@RequestBody AppUser user);

    @GetMapping("user/recommend")
    Result<List<RecommendVO>> getRecommend(@RequestBody RecommendDTO userId);

    @GetMapping("financial")
    Result<List<FinancialSimpleVO>> getFinancialList(@RequestBody PageDTO pageDTO, @RequestParam("id") BigInteger id);

    @GetMapping("financial/partake")
    Result<List<FinancialUserPartakeVO>> getFinancialPartakeList(@RequestBody FinancialPartakeDTO financialPartakeDTO, @RequestParam("userId") BigInteger userId);

    @GetMapping("financial/balance")
    Result<FinancialBalanceVO> getFinancialBalance(@RequestParam("userId") BigInteger userId);

    @GetMapping("financial/{id}")
    Result<FinancialDetailVO> getFinancialDetail(@PathVariable("id") BigInteger id, @RequestParam("userId") BigInteger userId);

    @GetMapping("financial/partake/{id}")
    Result<FinancialPartakeDetailVO> getPartakeDetail(@PathVariable("id") BigInteger id, @RequestParam("userId") BigInteger userId);

    @GetMapping("financial/partake/{financialId}/detail")
    Result<List<FinancialPartakeListVO>> getPartakeList(@PathVariable("financialId") BigInteger financialId, @RequestBody FinancialPartakeListDTO dto, @RequestParam("userId") BigInteger userId);

    @PostMapping("financial/{id}")
    Result<Boolean> buyFinancial(@PathVariable("id") BigInteger id, @RequestBody FinancialBuyDTO financialBuyDTO, @RequestParam("userId") BigInteger userId);

    @PostMapping("financial/partake/{id}")
    Result<Boolean> unlockPartake(@PathVariable("id") BigInteger id, @RequestParam("userId") BigInteger userId);

    @PutMapping("user/sign")
    Result<Boolean> sign(@RequestParam("userId") BigInteger userId);

    @GetMapping("user/sign")
    Result<Boolean> getSign(@RequestParam("userId") BigInteger userId);

    @GetMapping("appInfo/{appType}")
    Result<AppInfo> getApp(@PathVariable("appType") String appType);

}
