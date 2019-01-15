package com.mvc.cryptovault.app.service;

import com.mvc.cryptovault.app.bean.dto.PageDTO;
import com.mvc.cryptovault.app.feign.ConsoleRemoteService;
import com.mvc.cryptovault.common.bean.dto.FinancialBuyDTO;
import com.mvc.cryptovault.common.bean.dto.FinancialPartakeDTO;
import com.mvc.cryptovault.common.bean.dto.FinancialPartakeListDTO;
import com.mvc.cryptovault.common.bean.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class FinancialService {

    @Autowired
    ConsoleRemoteService remoteService;

    public List<FinancialSimpleVO> getList(PageDTO pageDTO, BigInteger id) {
        return remoteService.getFinancialList(pageDTO, id).getData();
    }

    public List<FinancialUserPartakeVO> getFinancialPartakeList(FinancialPartakeDTO financialPartakeDTO, BigInteger userId) {
        return remoteService.getFinancialPartakeList(financialPartakeDTO, userId).getData();
    }

    public FinancialBalanceVO getFinancialBalance(BigInteger userId) {
        return remoteService.getFinancialBalance(userId).getData();
    }

    public FinancialDetailVO getFinancialDetail(BigInteger id, BigInteger userId) {
        return remoteService.getFinancialDetail(id, userId).getData();
    }

    public FinancialPartakeDetailVO getPartakeDetail(BigInteger id, BigInteger userId) {
        return remoteService.getPartakeDetail(id, userId).getData();
    }

    public List<FinancialPartakeListVO> getPartakeList(BigInteger id, FinancialPartakeListDTO financialPartakeListDTO, BigInteger userId) {
        return remoteService.getPartakeList(id, financialPartakeListDTO, userId).getData();
    }

    public Boolean buy(BigInteger id, FinancialBuyDTO financialBuyDTO, BigInteger userId) {
        return remoteService.buyFinancial(id, financialBuyDTO, userId).getData();
    }

    public Boolean unlockPartake(BigInteger id, BigInteger userId) {
        return remoteService.unlockPartake(id, userId).getData();
    }

}
