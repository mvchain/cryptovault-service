package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.AdminWallet;
import com.mvc.cryptovault.common.bean.BlockSign;
import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.util.BaseContextHandler;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.bean.Balance;
import com.mvc.cryptovault.console.bean.UsdtTransaction;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.Block;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author qiyichen
 * @create 2018/11/29 14:03
 */
@Service("UsdtService")
@Transactional(rollbackFor = RuntimeException.class)
public class UsdtService extends BlockService {

    @Autowired
    BtcdClient btcdClient;
    @Autowired
    BlockSignService blockSignService;
    @Value("${usdt.propId}")
    private Integer propId;
    private static String nowHash = "";

    @Override
    public BigInteger getNonce(Map<String, BigInteger> nonceMap, String address) throws IOException {
        return null;
    }

    @Override
    public BigInteger getEthEstimateTransferFrom(String contractAddress, String from, String to) throws IOException {
        return null;
    }

    @Override
    public BigInteger getEthEstimateApprove(String contractAddress, String from, String to) throws IOException {
        return null;
    }

    @Override
    public void send(AdminWallet hot, String address, BigDecimal fromWei) throws IOException {

    }

    @Override
    public BigDecimal getBalance(String tokenName) {
        return null;
    }

    @Override
    public BigInteger getEthEstimateTransfer(String tokenContractAddress, String toAddress, String address, BigDecimal value) throws IOException {
        return null;
    }

    @Override
    public void run(String... args) throws Exception {
        executorService.execute(() -> {
            BaseContextHandler.set("thread-key", "btcOldListener");
            try {
                oldListener();
            } catch (Exception e) {
                oldListener();
            }
        });
        executorService.execute(() -> {
            BaseContextHandler.set("thread-key", "btcSignJob");
            try {
                signJob();
            } catch (Exception e) {
                signJob();
            }
        });
    }

    private void signJob() {

        while (true) {
            try {
                BlockSign sign = blockSignService.findOneByToken("BTC");
                sign(sign);
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sign(BlockSign sign) {
        try {
            if (null == sign) {
                return;
            }
            String result = btcdClient.sendRawTransaction(sign.getSign());
            sign.setHash(result);
            sign.setStatus(1);
            blockSignService.update(sign);
            blockTransactionService.updateHash(sign.getOrderId(), result);
        } catch (Exception e) {
            sign.setStatus(9);
            sign.setResult(e.getMessage());
            if (StringUtils.isNotBlank(sign.getOrderId())) {
                //更新区块交易表
                String message = "交易失败";
                String data = e.getMessage();
                updateError(sign.getOrderId(), message, data);
            }
        }
    }

    private BlockTransaction blockTransaction(UsdtTransaction tx) throws IOException {
        BlockTransaction transaction = new BlockTransaction();
        BigDecimal value = new BigDecimal(tx.getAmount());
        CommonAddress address = isOurAddress(tx.getSendingaddress(), tx.getReferenceaddress());
        //非内部地址忽略
        if (null == address) {
            return null;
        }
        Long time = System.currentTimeMillis();
        transaction.setFromAddress(tx.getSendingaddress());
        transaction.setToAddress(tx.getReferenceaddress());
        transaction.setHash(tx.getTxid());
        transaction.setUpdatedAt(time);
        transaction.setTokenType("BTC");
        transaction.setCreatedAt(time);
        transaction.setValue(value);
        transaction.setUserId(address.getUserId());
        transaction.setOrderNumber("");
        transaction.setFee(new BigDecimal(tx.getFee()));
        transaction.setHeight(tx.getBlock());
        transaction.setStatus(tx.getConfirmations().compareTo(BigInteger.valueOf(6)) >= 0 ? 2 : 1);
        transaction.setTransactionStatus(tx.getConfirmations().compareTo(BigInteger.valueOf(6)) >= 0 ? 5 : 4);
        //根据地址判断操作类型
        if (address.getUserId().equals(BigInteger.ZERO)) {
            transaction.setOprType(9);
        } else if (tx.getReferenceaddress().equalsIgnoreCase(address.getAddress())) {
            transaction.setOprType(1);
        } else {
            transaction.setOprType(2);
        }
        transaction.setTokenId(BusinessConstant.BASE_TOKEN_ID_USDT);
        return transaction;
    }

    private void oldListener() {
        String lastNumber = getHeight();
        Block block = null;
        while (true) {
            try {
                Thread.sleep(100);
                String height = btcdClient.getBlockChainInfo().getBestBlockHash();
                block = btcdClient.getBlock(height);
                Boolean ignore = isIgnore(lastNumber, block, height);
                if (ignore) {
                    continue;
                }
                List<String> txList = btcdClient.getBlock(block.getHash()).getTx();
                readTxList(txList);
                nowHash = null == block ? nowHash : block.getHash();
                updateStatus(block.getHeight().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readTxList(List<String> txList) {
        for (String txId : txList) {
            try {
                UsdtTransaction tx = null;
                Object txStr = btcdClient.remoteCall("omni_gettransaction", Arrays.asList(txId));
                tx = JSON.parseObject(String.valueOf(txStr), UsdtTransaction.class);
                BlockTransaction trans = blockTransaction(tx);
                if (null != trans) {
                    saveOrUpdate(trans);
                    BigDecimal fromValue = getBalance(trans.getFromAddress(), trans.getTokenId());
                    BigDecimal toValue = getBalance(trans.getToAddress(), trans.getTokenId());
                    updateAddressBalance(trans.getTokenId(), trans.getFromAddress(), fromValue);
                    updateAddressBalance(trans.getTokenId(), trans.getToAddress(), toValue);
                }
                updateAddressBalance(trans.getTokenId(), trans.getFromAddress(), getBalance(trans.getFromAddress(), trans.getTokenId()));
                updateAddressBalance(trans.getTokenId(), trans.getToAddress(), getBalance(trans.getToAddress(), trans.getTokenId()));
            } catch (Exception e) {
                // not mine transaction
            }
        }
    }

    @NotNull
    private Boolean isIgnore(String lastNumber, Block block, String height) {
        Boolean ignore = false;
        if (lastNumber.equals(height)) {
            ignore = true;
        }
        if (block.getConfirmations() == -1) {
            redisTemplate.opsForValue().set(RedisConstant.USDT_LAST_HEIGHT, block.getPreviousBlockHash());
            ignore = true;
        }
        if (nowHash.equalsIgnoreCase(block.getHash())) {
            ignore = true;
        }
        return ignore;
    }

    private BigDecimal getBalance(String fromAddress, BigInteger tokenId) {
        try {
            if (StringUtils.isBlank(fromAddress)) {
                return BigDecimal.ZERO;
            }
            if (tokenId.equals(BusinessConstant.BASE_TOKEN_ID_USDT)) {
                Object result = btcdClient.remoteCall("omni_getbalance", Arrays.asList(fromAddress, propId));
                Balance balance = JSON.parseObject(String.valueOf(result), Balance.class);
                return new BigDecimal(balance.getBalance());
            } else {
                btcdClient.setAccount(fromAddress, fromAddress);
                return btcdClient.getBalance(fromAddress);
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private String getHeight() {
        String lastNumber = redisTemplate.opsForValue().get(RedisConstant.USDT_LAST_HEIGHT);
        if (StringUtils.isBlank(lastNumber)) {
            String height = null;
            try {
                height = btcdClient.getBlockChainInfo().getBestBlockHash();
            } catch (Exception e) {
                e.printStackTrace();
            }
            lastNumber = String.valueOf(height);
            redisTemplate.opsForValue().set(RedisConstant.USDT_LAST_HEIGHT, lastNumber);
        }
//        redisTemplate.opsForValue().set(RedisConstant.USDT_LAST_HEIGHT, "00000000000001244f8b6ca6463c5e46b4e44cddabd8face13c271919b29d66a");
        return lastNumber;
    }

    private void updateStatus(String lastNumber) {
        BigInteger height = NumberUtils.createBigInteger(lastNumber).subtract(BigInteger.valueOf(6));
        Condition condition = new Condition(BlockTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "status = ", 1);
        ConditionUtil.andCondition(criteria, "height <= ", height);
        ConditionUtil.andCondition(criteria, "token_type = ", "BTC");
        PageHelper.startPage(1, 10);
        List<BlockTransaction> blockTransaction = blockTransactionService.findByCondition(condition);
        blockTransaction.forEach(obj -> {
            blockTransactionService.updateSuccess(obj);
        });
    }
}
