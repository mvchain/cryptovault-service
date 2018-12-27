package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AppUserAddress;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.AppUserAddressMapper;
import com.mvc.cryptovault.console.dao.CommonAddressMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AppUserAddressService extends AbstractService<AppUserAddress> implements BaseService<AppUserAddress> {

    @Autowired
    AppUserAddressMapper appUserAddressMapper;
    @Autowired
    CommonAddressMapper commonAddressMapper;
    @Autowired
    CommonTokenService commonTokenService;

    public String getAddress(BigInteger userId, BigInteger tokenId) {
        String key = "AppUserAddress".toUpperCase() + "_" + userId;
        if (redisTemplate.boundHashOps(key).size() == 0 || null == redisTemplate.boundHashOps(key).get(String.valueOf(tokenId))) {
            AppUserAddress condition = new AppUserAddress();
            condition.setUserId(userId);
            condition.setTokenId(tokenId);
            AppUserAddress address = findOneByEntity(condition);
            if (null == address) {
                address = createAddress(userId, commonTokenService.findById(tokenId));
            }
            redisTemplate.boundHashOps(key).put(String.valueOf(tokenId), address.getAddress());
        }
        String address = (String) redisTemplate.boundHashOps(key).get(String.valueOf(tokenId));
        return address;
    }

    public AppUserAddress createAddress(BigInteger userId, CommonToken token) {
        AppUserAddress appUserAddress = new AppUserAddress();
        String address = "";
        //基础货币没有地址(余额类型)
        if (StringUtils.isNotBlank(token.getTokenType())) {
            CommonAddress commonAddress = commonAddressMapper.findUnUsed(token.getTokenType());
            commonAddress.setUsed(1);
            commonAddress.setUserId(userId);
            commonAddress.setAddressType(token.getTokenName());
            commonAddress.setApprove(0);
            commonAddressMapper.updateByPrimaryKeySelective(commonAddress);
            address = commonAddress.getAddress();
        }
        appUserAddress.setTokenId(token.getId());
        appUserAddress.setUserId(userId);
        appUserAddress.setAddress(address);
        save(appUserAddress);
        return appUserAddress;
    }
}