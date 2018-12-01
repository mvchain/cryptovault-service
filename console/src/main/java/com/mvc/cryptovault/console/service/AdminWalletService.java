package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.AdminWallet;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import org.springframework.stereotype.Service;

@Service
public class AdminWalletService extends AbstractService<AdminWallet> implements BaseService<AdminWallet> {

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
}