package com.mvc.cryptovault.app.service;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.vo.TokenDetailVO;
import com.mvc.cryptovault.common.bean.vo.TokenRatioVO;
import com.mvc.cryptovault.app.feign.TokenRemoteService;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.bean.CommonTokenPrice;
import com.mvc.cryptovault.common.bean.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
public class TokenService {

    @Autowired
    TokenRemoteService tokenRemoteService;

    public List<TokenDetailVO> getTokens(BigInteger timestamp) {
        Result<PageInfo<CommonToken>> listData = tokenRemoteService.all(1, 0, 999, timestamp);
        ArrayList<TokenDetailVO> result = new ArrayList<>(listData.getData().getList().size());
        for (CommonToken token : listData.getData().getList()) {
            TokenDetailVO vo = new TokenDetailVO();
            vo.setTokenImage(token.getTokenImage());
            vo.setTokenCnName(token.getTokenCnName());
            vo.setTokenEnName(token.getTokenEnName());
            vo.setTokenId(token.getId());
            vo.setTokenName(token.getTokenName());
            result.add(vo);
        }
        return result;
    }


    public List<TokenRatioVO> getBase() {
        Result<PageInfo<CommonTokenPrice>> listData = tokenRemoteService.price();
        List<TokenRatioVO> result = new ArrayList<>(listData.getData().getList().size());
        for (CommonTokenPrice token : listData.getData().getList()) {
            TokenRatioVO vo = new TokenRatioVO();
            vo.setTokenName(token.getTokenName());
            vo.setTokenId(token.getTokenId());
            vo.setRatio(token.getTokenPrice());
            result.add(vo);
        }
        return result;

    }
}
