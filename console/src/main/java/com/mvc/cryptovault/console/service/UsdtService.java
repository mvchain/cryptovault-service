package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.util.BaseContextHandler;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.bean.Balance;
import com.mvc.cryptovault.console.bean.UsdtTransaction;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.util.btc.BtcAction;
import com.mvc.cryptovault.console.util.btc.entity.TetherBalance;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.Output;
import com.neemre.btcdcli4j.core.domain.OutputOverview;
import com.neemre.btcdcli4j.core.domain.SignatureResult;
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
import java.util.*;
import java.util.stream.Collectors;

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
    private AdminWallet hotWallet = null;
    @Autowired
    CommonTokenService commonTokenService;
    //发送usdt时必然会带上这笔金额,因此发送手续费时需要额外发送这笔数量,否则会从预设手续费中扣除从而导致和期望结果不一致
    private final BigDecimal USDT_LIMIT_FEE = new BigDecimal("0.00000546");

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
            SignatureResult hex = btcdClient.signRawTransaction(sign.getSign());
            if (hex.getComplete()) {
                String result = btcdClient.sendRawTransaction(hex.getHex());
                sign.setHash(result);
                sign.setStatus(1);
                blockSignService.update(sign);
                blockTransactionService.updateHash(sign.getOrderId(), result);
            } else {
                sign.setStatus(9);
                sign.setResult("签名失败");
                blockSignService.update(sign);
                if (StringUtils.isNotBlank(sign.getOrderId())) {
                    //更新区块交易表
                    String message = "交易失败";
                    String data = "签名失败";
                    updateError(sign.getOrderId(), message, data);
                }
            }
        } catch (Exception e) {
            sign.setStatus(9);
            sign.setResult(e.getMessage());
            blockSignService.update(sign);
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
                block = btcdClient.getBlock(lastNumber);
                Boolean ignore = isIgnore(lastNumber, block, height);
                if (ignore) {
                    continue;
                }
                List<String> txList = btcdClient.getBlock(block.getHash()).getTx();
                readTxList(txList);
                nowHash = null == block ? nowHash : block.getHash();
                lastNumber = block.getNextBlockHash();
                redisTemplate.opsForValue().set(RedisConstant.USDT_LAST_HEIGHT, lastNumber);
                updateStatus(block.getHeight().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void readTxList(List<String> txList) {
        for (String txId : txList) {
            try {
                if (txId.equalsIgnoreCase("cf61368aee250714856873b96146bd972e7e3432507b08e9627d437182bd1c44")) {
                    System.out.println(1111);
                }

                UsdtTransaction tx = null;
                Object txStr = btcdClient.remoteCall("omni_gettransaction", Arrays.asList(txId));
                tx = JSON.parseObject(String.valueOf(txStr), UsdtTransaction.class);
                if (null == tx) {
                    continue;
                }
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


    private AdminWallet getHotWallet() {
        try {
            if (null != hotWallet) {
                return hotWallet;
            }
            AdminWallet wallet = adminWalletService.getBtcHot();
            if (null != wallet) {
                hotWallet = wallet;
                return wallet;
            }
            String address = btcdClient.getNewAddress();
            String pvKey = btcdClient.dumpPrivKey(address);
            btcdClient.setAccount(address, address);
            wallet = new AdminWallet();
            wallet.setIsHot(1);
            wallet.setBlockType(2);
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setAddress(address);
            wallet.setPvKey(pvKey);
            adminWalletService.save(wallet);
            hotWallet = wallet;
            return wallet;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 批量发送时手续费默认为单次发送的一半(按笔数算)
     *
     * @param wallet
     * @param token
     * @param addresses
     */
    private void sendBtc(AdminWallet wallet, CommonToken token, List<String> addresses) throws BitcoindException, CommunicationException {
        Map<String, BigDecimal> output = new HashMap<>();
        List<OutputOverview> input = new ArrayList<>(addresses.size());
        List<Output> listUnspent = btcdClient.listUnspent();
        //热钱包中的总余额
        BigDecimal total = btcdClient.getBalance();
        BigDecimal use = BigDecimal.ZERO;
        listUnspent = listUnspent.stream().filter(obj -> obj.getSpendable() == true).collect(Collectors.toList());
        BigDecimal fee = BigDecimal.ZERO.add(BigDecimal.valueOf(new Float(String.valueOf(token.getTransaferFee())) * addresses.size() / 2));
        for (Output obj : listUnspent) {
            //使用后余额也还原到该地址
            input.add(obj);
        }
        for (String address : addresses) {
            BigDecimal value = new BigDecimal(String.valueOf(token.getTransaferFee()));
            use = use.add(value);
            output.put(address, value);
        }
        //找零 = 总余额 - 发送余额 - 预设手续费
        output.put(wallet.getAddress(), total.subtract(use).subtract(fee));
        String row = btcdClient.createRawTransaction(input, output);
        SignatureResult res = btcdClient.signRawTransaction(row);
        if (res.getComplete()) {
            btcdClient.sendRawTransaction(res.getHex());
        }
    }

    public void senBtcGas() {
        try {
            AdminWallet wallet = getHotWallet();
            Boolean ignore = getIgnore(wallet);
            if (ignore) {
                return;
            }
            CommonToken token = commonTokenService.findById(BusinessConstant.BASE_TOKEN_ID_USDT);
            List<TetherBalance> list = BtcAction.getTetherBalance();
            List<String> addresses = new ArrayList<>(list.size());
            for (TetherBalance tetherBalance : list) {
                String pvKey = btcdClient.dumpPrivKey(tetherBalance.getAddress());
                System.out.println(tetherBalance.getAddress() + ":" + pvKey);
                //需要发送手续费的地址,不在本系统中的地址也直接忽略,由于无法签名.对于数额过小的也直接忽略(动态设置),已存在足够手续费也忽略
                CommonAddress address = commonAddressService.findOneBy("address", tetherBalance.getAddress());
                BigDecimal btcBalance = btcdClient.getBalance(tetherBalance.getAddress());
                Boolean flag = tetherBalance.getBalance().compareTo(BigDecimal.ZERO) <= 0 ||
                        null == address ||
                        tetherBalance.getBalance().compareTo(BigDecimal.valueOf(token.getHold())) < 0 ||
                        btcBalance.compareTo(new BigDecimal(String.valueOf(token.getTransaferFee()))) >= 0;
                if (flag) {
                    continue;
                }
                //将所有交易打包成一次交易
                addresses.add(tetherBalance.getAddress());
            }
            if (addresses.size() > 0) {
                sendBtc(wallet, token, addresses);
            }
        } catch (BitcoindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CommunicationException e) {
            e.printStackTrace();
        }

    }

    @NotNull
    private Boolean getIgnore(AdminWallet wallet) throws BitcoindException, CommunicationException {
        Boolean ignore = false;
        if (null == wallet) {
            ignore = true;
        }
        BigDecimal balance = btcdClient.getBalance(wallet.getAddress());
        if (balance.compareTo(BigDecimal.ZERO) <= 0) {
            ignore = true;
        }
        return ignore;
    }
}
