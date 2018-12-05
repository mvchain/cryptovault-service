package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AdminWallet;
import com.mvc.cryptovault.common.bean.BlockHeight;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.BlockHeightMapper;
import com.mvc.cryptovault.console.dao.CommonAddressMapper;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.protocol.Web3j;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class BlockHeightService extends AbstractService<BlockHeight> implements BaseService<BlockHeight> {
    @Autowired
    BlockHeightMapper blockHeightMapper;
    @Autowired
    CommonAddressMapper commonAddressMapper;
    @Autowired
    AdminWalletService adminWalletService;
    @Autowired
    Web3j web3j;
    @Autowired
    BtcdClient btcdClient;

    @Async
    public void importAddress(List<CommonAddress> list, String fileName) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        String key = RedisConstant.ADDRESS_IMPORT + fileName;
        List<AdminWallet> wallets = adminWalletService.findAll();
        list.stream().forEach(obj -> {
            obj.setUsed(0);
            obj.setBalance(BigDecimal.ZERO);
            obj.setApprove(0);
            try {
                commonAddressMapper.insert(obj);
                if (obj.getTokenType().equalsIgnoreCase("BTC")) {
                    btcdClient.importAddress(obj.getAddress(), obj.getAddress(), false);
                }
            } catch (Exception e) {
                //address exist
                e.printStackTrace();
            }
        });
        PageHelper.clearPage();
        initWallet(wallets);
        redisTemplate.delete(key);
    }

    private void initWallet(List<AdminWallet> wallets) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Long coldEth = wallets.stream().filter(obj -> obj.getIsHot() == 0 && obj.getBlockType() == 1).count();
        Long hotEth = wallets.stream().filter(obj -> obj.getIsHot() == 1 && obj.getBlockType() == 1).count();
        Long coldBtc = wallets.stream().filter(obj -> obj.getIsHot() == 0 && obj.getBlockType() == 0).count();
        if (coldEth.equals(0L)) {
            CommonAddress address = commonAddressMapper.findUnUsed("ETH");
            if (null != address) {
                AdminWallet wallet = new AdminWallet();
                wallet.setAddress(address.getAddress());
                wallet.setBalance(BigDecimal.ZERO);
                wallet.setIsHot(0);
                wallet.setPvKey("");
                wallet.setBlockType(1);
                adminWalletService.save(wallet);
                address.setUsed(1);
                address.setUserId(BigInteger.ZERO);
                address.setAddressType("ETH");
                commonAddressMapper.updateByPrimaryKeySelective(address);
            }
        }
        if (hotEth.equals(0L)) {
            CommonAddress address = commonAddressMapper.findUnUsed("ETH");
            if (null != address) {
                ECKeyPair keys = Keys.createEcKeyPair();
                String addr = Credentials.create(keys).getAddress();
                AdminWallet wallet = new AdminWallet();
                wallet.setAddress(addr);
                wallet.setBalance(BigDecimal.ZERO);
                wallet.setIsHot(1);
                wallet.setPvKey(String.valueOf(keys.getPrivateKey()));
                wallet.setBlockType(1);
                adminWalletService.save(wallet);
            }
        }
        if (coldBtc.equals(0L)) {
            CommonAddress address = commonAddressMapper.findUnUsed("BTC");
            if (null != address) {
                AdminWallet wallet = new AdminWallet();
                wallet.setAddress(address.getAddress());
                wallet.setBalance(BigDecimal.ZERO);
                wallet.setIsHot(0);
                wallet.setPvKey("");
                wallet.setBlockType(2);
                adminWalletService.save(wallet);
                address.setUsed(1);
                address.setUserId(BigInteger.ZERO);
                address.setAddressType("BTC");
                commonAddressMapper.updateByPrimaryKeySelective(address);
            }
        }
        adminWalletService.updateAllCache();
    }

    public Integer accountCount(String tokenType) {
        CommonAddress commonAddress = new CommonAddress();
        commonAddress.setUsed(0);
        commonAddress.setTokenType(tokenType);
        Integer count = commonAddressMapper.selectCount(commonAddress);
        return count;
    }
}