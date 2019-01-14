package com.mvc.cryptovault.dashboard.service;

import com.github.pagehelper.PageInfo;
import com.mvc.cryptovault.common.bean.dto.PageDTO;
import com.mvc.cryptovault.common.bean.vo.DPairVO;
import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.dashboard.bean.dto.DTokenDTO;
import com.mvc.cryptovault.common.dashboard.bean.dto.OverTransactionDTO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenSettingVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenTransSettingVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.DTokenVO;
import com.mvc.cryptovault.common.dashboard.bean.vo.OverTransactionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/19 19:58
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class TokenService extends BaseService {


    public List<DTokenVO> findTokens(String tokenName, Integer blockType) {
        Result<List<DTokenVO>> result = remoteService.findTokens(tokenName, blockType);
        return result.getData();
    }

    public Boolean newToken(DTokenDTO dTokenDTO) {
        Result<Boolean> result = remoteService.newToken(dTokenDTO);
        return result.getData();
    }

    public Boolean updateToken(DTokenDTO dTokenDTO) {
        Result<Boolean> result = remoteService.updateToken(dTokenDTO);
        return result.getData();
    }

    public Boolean tokenSetting(DTokenSettingVO dto) {
        Result<Boolean> result = remoteService.tokenSetting(dto);
        return result.getData();
    }

    public PageInfo<DTokenSettingVO> getTokenSettings(String tokenName, PageDTO pageDTO) {
        Result<PageInfo<DTokenSettingVO>> result = remoteService.getTokenSettings(pageDTO, tokenName);
        return result.getData();
    }

    public DTokenSettingVO getTokenSetting(BigInteger id) {
        Result<DTokenSettingVO> result = remoteService.getTokenSetting(id);
        return result.getData();
    }

    public DTokenTransSettingVO getTransSetting(BigInteger id) {
        Result<DTokenTransSettingVO> result = remoteService.getTransSetting(id);
        return result.getData();
    }

    public Boolean setTransSetting(DTokenTransSettingVO dto) {
        Result<Boolean> result = remoteService.setTransSetting(dto);
        return result.getData();
    }

    public PageInfo<OverTransactionVO> overList(OverTransactionDTO overTransactionDTO) {
        Result<PageInfo<OverTransactionVO>> result = remoteService.overList(overTransactionDTO);
        return result.getData();
    }

    public DTokenDTO getToken(BigInteger id) {
        Result<DTokenDTO> result = remoteService.getToken(id);
        return result.getData();
    }

    public List<DPairVO> getPair() {
        Result<List<DPairVO>> result = remoteService.getPair();
        return result.getData();
    }
}
