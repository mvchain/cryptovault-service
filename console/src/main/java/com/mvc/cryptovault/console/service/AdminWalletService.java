package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AdminWallet;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class AdminWalletService extends AbstractService<AdminWallet> implements BaseService<AdminWallet> {

    @Autowired
    BtcdClient btcdClient;

    public String getAddress(Integer blockType, Integer isHot){
        AdminWallet adminWallet = new AdminWallet();
        adminWallet.setIsHot(isHot);
        adminWallet.setBlockType(blockType);
        AdminWallet wallet = mapper.selectOne(adminWallet);
        if(null == wallet){
            return null;
        }
        return wallet.getAddress();
    }

    public AdminWallet getEthHot() {
        AdminWallet adminWallet = new AdminWallet();
        adminWallet.setIsHot(1);
        adminWallet.setBlockType(1);
        return mapper.selectOne(adminWallet);
    }

    public AdminWallet getEthCold() {
        AdminWallet adminWallet = new AdminWallet();
        adminWallet.setIsHot(0);
        adminWallet.setBlockType(1);
        return mapper.selectOne(adminWallet);
    }

    public CommonAddress isCold(String from, String to) {
        CommonAddress address = null;
        AdminWallet wallet = new AdminWallet();
        wallet.setIsHot(1);
        List<AdminWallet> list = findByEntity(wallet);
        for (AdminWallet obj : list) {
            if (obj.getAddress().equalsIgnoreCase(from) || obj.getAddress().equalsIgnoreCase(to)) {
                address = new CommonAddress();
                address.setUserId(BigInteger.ZERO);
                address.setAddress(obj.getAddress());
                break;
            }
        }
        return address;
    }

    public AdminWallet getBtcCold() {
        AdminWallet adminWallet = new AdminWallet();
        adminWallet.setIsHot(0);
        adminWallet.setBlockType(2);
        return mapper.selectOne(adminWallet);
    }

    public AdminWallet getBtcHot() {
        AdminWallet adminWallet = new AdminWallet();
        adminWallet.setIsHot(1);
        adminWallet.setBlockType(2);
        return mapper.selectOne(adminWallet);
    }
}