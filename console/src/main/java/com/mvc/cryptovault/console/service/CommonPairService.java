package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.CommonPair;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.CommonPairMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CommonPairService extends AbstractService<CommonPair> implements BaseService<CommonPair> {

    @Autowired
    CommonPairMapper commonPairMapper;
    @Autowired
    CommonTokenService commonTokenService;

    public void updatePair(BigInteger tokenId, Integer vrt, Integer balance) {
        CommonPair pair = new CommonPair();
        pair.setTokenId(tokenId);
        pair.setBaseTokenId(BusinessConstant.BASE_TOKEN_ID_VRT);
        CommonPair pairVrt = findOneByEntity(pair);
        pair.setBaseTokenId(BusinessConstant.BASE_TOKEN_ID_BALANCE);
        CommonPair partBalance = findOneByEntity(pair);
        partBalance.setStatus(balance);
        pairVrt.setStatus(vrt);
        update(partBalance);
        update(pairVrt);
        updateAllCache();
        updateCache(partBalance.getId());
        updateCache(pairVrt.getId());
    }

    public void insertPair(BigInteger tokenId, String tokenName) {
        CommonToken vrt = commonTokenService.findById(BusinessConstant.BASE_TOKEN_ID_VRT);
        CommonToken balance = commonTokenService.findById(BusinessConstant.BASE_TOKEN_ID_BALANCE);
        CommonPair pairVrt = new CommonPair();
        pairVrt.setBaseTokenId(BusinessConstant.BASE_TOKEN_ID_VRT);
        pairVrt.setFee(0f);
        pairVrt.setTokenId(tokenId);
        pairVrt.setTokenName(tokenName);
        pairVrt.setBaseTokenName(vrt.getTokenName());
        pairVrt.setStatus(0);
        pairVrt.setPairName(tokenName + "/" + vrt.getTokenName());
        CommonPair partBalance = new CommonPair();
        partBalance.setBaseTokenId(BusinessConstant.BASE_TOKEN_ID_BALANCE);
        partBalance.setFee(0f);
        partBalance.setTokenId(tokenId);
        partBalance.setTokenName(tokenName);
        partBalance.setBaseTokenName(balance.getTokenName());
        partBalance.setPairName(tokenName + "/" + balance.getTokenName());
        partBalance.setStatus(0);
        save(pairVrt);
        save(partBalance);
        updateAllCache();
        updateCache(partBalance.getTokenId());
        updateCache(pairVrt.getTokenId());
    }

    public BigInteger findByTokenId(BigInteger baseTokenId, BigInteger tokenId) {
        CommonPair pair = new CommonPair();
        pair.setTokenId(tokenId);
        pair.setBaseTokenId(baseTokenId);
        pair = findOneByEntity(pair);
        return null == pair ? BigInteger.ZERO : pair.getId();
    }
}