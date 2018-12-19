package com.mvc.cryptovault.console.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mvc.cryptovault.common.bean.AdminWallet;
import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.console.config.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.*;

@Component
public abstract class BlockService implements CommandLineRunner {

    @Autowired
    protected BlockTransactionService blockTransactionService;
    @Autowired
    protected CommonAddressService commonAddressService;
    @Autowired
    private CommonTokenService commonTokenService;
    @Autowired
    protected StringRedisTemplate redisTemplate;
    @Autowired
    AdminWalletService adminWalletService;

    protected static volatile ExecutorService executorService;

    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("address-pool-%d").build();
        executorService = new ThreadPoolExecutor(10, 10, 10, TimeUnit.MINUTES,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.DiscardPolicy());
    }

    protected Boolean saveOrUpdate(BlockTransaction blockTransaction) {
        if (null == blockTransaction) {
            return null;
        }
        BlockTransaction trans = blockTransactionService.findOneBy("hash", blockTransaction.getHash());
        if (null == trans) {
            return blockTransactionService.saveTrans(blockTransaction);
        } else {
            blockTransactionService.updateTrans(trans, blockTransaction);
            return false;
        }
        //TODO 添加通用订单以及推送
    }

    protected CommonAddress isOurAddress(String from, String to) {
        if (StringUtils.isAnyBlank(from, to)) {
            return null;
        }
        CommonAddress cold = adminWalletService.isCold(from, to);
        if (null != cold) {
            //热钱包操作
            return cold;
        }
        //返回地址信息。充值和提现时返回用户地址，汇总和钱包操作则返回非用户地址（userId为0）
        CommonAddress fromAddress = commonAddressService.findOneBy("address", from);
        CommonAddress toAddress = commonAddressService.findOneBy("address", to);
        if (null == fromAddress && null == toAddress) {
            return null;
        } else if (null != fromAddress && null == toAddress) {
            return fromAddress;
        } else if (null != toAddress && null == fromAddress) {
            return toAddress;
        } else {
            //两个地址都为钱包地址，则为钱包操作
            if (null == fromAddress.getUserId() || fromAddress.getUserId().equals(BigInteger.ZERO)) {
                fromAddress.setUserId(BigInteger.ZERO);
                return fromAddress;
            } else {
                return toAddress;
            }
        }
    }

    protected void updateAddressBalance(BigInteger tokenId, String address, BigDecimal value) {
        CommonAddress addr = commonAddressService.findOneBy("address", address);
        CommonToken token = commonTokenService.findById(tokenId);
        if (null != addr && addr.getAddressType().equalsIgnoreCase(token.getTokenName())) {
            addr.setBalance(value);
            commonAddressService.update(addr);
        }
    }

    protected void updateApprove(String address) {
        CommonAddress commonAddress = commonAddressService.findOneBy("address", address);
        if (null != commonAddress) {
            commonAddress.setApprove(1);
            commonAddressService.update(commonAddress);
        }
    }

    protected void updateError(String orderId, String message, String data) {
        BlockTransaction transaction = blockTransactionService.findOneBy("orderNumber", orderId);
        if (null != transaction) {
            transaction.setStatus(9);
            transaction.setTransactionStatus(6);
            transaction.setUpdatedAt(System.currentTimeMillis());
            transaction.setErrorData(data);
            transaction.setErrorMsg(message);
            blockTransactionService.update(transaction);
        }
    }

    public abstract BigInteger getNonce(Map<String, BigInteger> nonceMap, String address) throws IOException;

    public abstract BigInteger getEthEstimateTransferFrom(String contractAddress, String from, String to) throws IOException;

    public abstract BigInteger getEthEstimateApprove(String contractAddress, String from, String to) throws IOException;

    public abstract void send(AdminWallet hot, String address, BigDecimal fromWei) throws IOException;

    public abstract BigDecimal getBalance(String tokenName);

    public abstract BigInteger getEthEstimateTransfer(String tokenContractAddress, String toAddress, String address, BigDecimal value) throws IOException;

    public BlockService get(String tokenType){
        if("usdt".equalsIgnoreCase(tokenType)){
            return SpringContextUtil.getBean("UsdtService");
        } else {
            return SpringContextUtil.getBean("EthService");
        }
    };
}
