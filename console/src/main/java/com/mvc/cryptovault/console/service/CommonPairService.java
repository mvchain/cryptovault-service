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

    public void updatePair(BigInteger tokenId, Integer vrt) {
        CommonPair pair = new CommonPair();
        pair.setTokenId(tokenId);
        pair.setBaseTokenId(BusinessConstant.BASE_TOKEN_ID_VRT);
        CommonPair pairVrt = findOneByEntity(pair);
        if(null == pairVrt){
            CommonToken token = commonTokenService.findById(tokenId);
            insertPair(token.getId(), token.getTokenName());
            pairVrt = findOneByEntity(pair);
        }
        pairVrt.setStatus(vrt);
        update(pairVrt);
        updateAllCache();
        updateCache(pairVrt.getId());
    }

    public void insertPair(BigInteger tokenId, String tokenName) {
        CommonToken vrt = commonTokenService.findById(BusinessConstant.BASE_TOKEN_ID_VRT);
        CommonPair pairVrt = new CommonPair();
        pairVrt.setBaseTokenId(BusinessConstant.BASE_TOKEN_ID_VRT);
        pairVrt.setFee(0f);
        pairVrt.setTokenId(tokenId);
        pairVrt.setTokenName(tokenName);
        pairVrt.setBaseTokenName(vrt.getTokenName());
        pairVrt.setStatus(0);
        pairVrt.setPairName(tokenName + "/" + vrt.getTokenName());
        save(pairVrt);
        updateAllCache();
        updateCache(pairVrt.getTokenId());
    }

    public CommonPair findByTokenId(BigInteger baseTokenId, BigInteger tokenId) {
        CommonPair pair = new CommonPair();
        pair.setTokenId(tokenId);
        pair.setBaseTokenId(baseTokenId);
        pair = findOneByEntity(pair);
        return pair;
    }
}