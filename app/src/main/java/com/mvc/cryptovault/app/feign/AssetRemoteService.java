package com.mvc.cryptovault.app.feign;

import com.mvc.cryptovault.common.bean.dto.DebitDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionDTO;
import com.mvc.cryptovault.common.bean.dto.TransactionSearchDTO;
import com.mvc.cryptovault.common.bean.vo.TokenBalanceVO;
import com.mvc.cryptovault.common.bean.vo.TransactionDetailVO;
import com.mvc.cryptovault.common.bean.vo.TransactionSimpleVO;
import com.mvc.cryptovault.common.bean.vo.TransactionTokenVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@FeignClient("console")
public interface AssetRemoteService {

    @GetMapping("appUserBalance/{userId}")
    Result<List<TokenBalanceVO>> getAsset(@PathVariable("userId") BigInteger userId);

    @GetMapping("appUserBalance/sum/{userId}")
    Result<BigDecimal> getBalance(@PathVariable("userId") BigInteger userId);

    @GetMapping("appOrder/user")
    Result<List<TransactionSimpleVO>> getTransactions(@RequestParam("userId") BigInteger userId, @ModelAttribute TransactionSearchDTO transactionSearchDTO);

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
}
