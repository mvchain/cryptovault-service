package com.mvc.cryptovault.console.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mvc.cryptovault.common.bean.AdminWallet;
import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.CommonToken;
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
    private CommonAddressService commonAddressService;
    @Autowired
    private CommonTokenService commonTokenService;
    @Autowired
    protected StringRedisTemplate redisTemplate;

    protected static volatile ExecutorService executorService;

    static {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("address-pool-%d").build();
        executorService = new ThreadPoolExecutor(4, 4, 5L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.DiscardPolicy());
    }

    protected Boolean saveOrUpdate(BlockTransaction blockTransaction) {
        if (null == blockTransaction) {
            return null;
        }
        BlockTransaction trans = blockTransactionService.findOneBy("hash", blockTransaction.getHash());
        if (null == trans) {
            blockTransactionService.save(blockTransaction);
            if (blockTransactionService.findBy("hash", blockTransaction.getHash()).size() > 1) {
                //插入后如果数据重复则删除
                blockTransactionService.deleteById(blockTransaction.getId());
                return null;
            }
            return true;
        } else {
            trans.setTransactionStatus(blockTransaction.getTransactionStatus());
            //已经成功的记录不修改,防止余额重复累加
            if(trans.getStatus() != 2){
                trans.setStatus(blockTransaction.getStatus());
            }
            trans.setUpdatedAt(System.currentTimeMillis());
            trans.setHeight(blockTransaction.getHeight());
            trans.setFee(blockTransaction.getFee());
            trans.setErrorData(blockTransaction.getErrorData());
            trans.setFromAddress(blockTransaction.getFromAddress());
            trans.setErrorMsg(blockTransaction.getErrorMsg());
            blockTransactionService.update(trans);
            return false;
        }
        //TODO 添加通用订单以及推送
    }

    protected CommonAddress isOurAddress(String from, String to) {
        if (StringUtils.isAnyBlank(from, to)) {
            return null;
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
            if (fromAddress.getUserId().equals(BigInteger.ZERO)) {
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

    public abstract BigInteger getNonce(Map<String, BigInteger> nonceMap, String address) throws IOException;

    public abstract BigInteger getEthEstimateTransferFrom(String contractAddress, String from, String to) throws IOException;
    public abstract BigInteger getEthEstimateApprove(String contractAddress, String from, String to) throws IOException;

    public abstract void send(AdminWallet hot, String address, BigDecimal fromWei) throws IOException;

    public abstract BigDecimal getBalance(String tokenName);

    public abstract BigInteger getEthEstimateTransfer(String tokenContractAddress, String toAddress, String address, BigDecimal value) throws IOException;
}
