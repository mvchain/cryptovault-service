package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.util.BaseContextHandler;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiyichen
 * @create 2018/11/29 14:03
 */
@Service("EthService")
@Transactional(rollbackFor = RuntimeException.class)
@Primary
@Log4j
public class EthService extends BlockService {

    @Autowired
    Web3j web3j;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    ContractService contractService;
    @Autowired
    AdminWalletService adminWalletService;
    @Autowired
    BlockSignService blockSignService;

    /**
     * 执行transferFrom方法时发现没有approve时的等待时间
     */
    private Long APPROVE_WAIT = 1000 * 60L;
    /**
     * 最大允许汇总量,每汇总一次会减少,需要设置大一些
     */
    private BigInteger MAX_APPROVE = new BigInteger("10000000000000");
    /**
     * input存在内容时, 只有转账方法需要记录,其他如创建合约之类的不需要记录
     * TODO 测试网和正是王方法id不同, transfer和transfer from 都需要记录
     */
    private List<String> transferArr = Arrays.asList(new String[]{"0xa9059cbb"});
    private List<String> transferFromArr = Arrays.asList(new String[]{"0x23b872dd"});
    private List<String> approveArr = Arrays.asList(new String[]{"0x095ea7b3"});
    private Map<String, CommonToken> tokenMap = new ConcurrentHashMap<>();

    @Override
    @Async
    public void run(String... args) throws Exception {
        //block listener
        executorService.execute(() -> {
            BaseContextHandler.set("thread-key", "ethNewListener");
            try {
                newListener();
            } catch (Exception e) {
                newListener();
            }
        });
        executorService.execute(() -> {
            BaseContextHandler.set("thread-key", "ethOldListener");
            try {
                oldListener();
            } catch (Exception e) {
                oldListener();
            }
        });
        executorService.execute(() -> {
            BaseContextHandler.set("thread-key", "ethSignJob");
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
                BlockSign sign = blockSignService.findOneByToken("ETH");
                if (null != sign) {
                    if (sign.getOprType() == 0 && StringUtils.isNotBlank(sign.getContractAddress())) {
                        //汇总时先判断是否approve
                        AdminWallet cold = adminWalletService.getEthCold();
                        if (cold == null) {
                            sign.setStartedAt(System.currentTimeMillis() + APPROVE_WAIT);
                        } else {
                            BigInteger result = contractService.allowance(sign.getContractAddress(), sign.getToAddress(), cold.getAddress());
                            if (result.equals(BigInteger.ZERO)) {
                                sign.setStartedAt(System.currentTimeMillis() + APPROVE_WAIT);
                            } else {
                                sendRaw(sign);
                            }
                        }
                    } else {
                        sendRaw(sign);
                    }
                    blockSignService.update(sign);
                }
                Thread.sleep(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRaw(BlockSign sign) throws IOException {
        EthSendTransaction result = web3j.ethSendRawTransaction(sign.getSign()).send();
        if (null == result || (null != result.getError())) {
            if (null != result && result.getError().getMessage().indexOf("known transaction:") >= 0) {
                return;
            }
            sign.setStatus(9);
            sign.setResult(JSON.toJSONString(result.getError()));
            if (StringUtils.isNotBlank(sign.getOrderId())) {
                //更新区块交易表
                String message = null == result.getError() ? "交易失败" : result.getError().getMessage();
                String data = null == result.getError() ? "交易失败" : result.getError().getData();
                updateError(sign.getOrderId(), message, data);
            }
        } else {
            String hash = result.getTransactionHash();
            blockTransactionService.updateHash(sign.getOrderId(), hash);
        }
    }

    private void updateStatus(String lastNumber) {
        BigInteger height = NumberUtils.createBigInteger(lastNumber).subtract(BigInteger.valueOf(12));
        Condition condition = new Condition(BlockTransaction.class);
        Example.Criteria criteria = condition.createCriteria();
        ConditionUtil.andCondition(criteria, "status = ", 1);
        ConditionUtil.andCondition(criteria, "height <= ", height);
        ConditionUtil.andCondition(criteria, "token_type = ", "ETH");
        PageHelper.startPage(1, 10);
        List<BlockTransaction> blockTransaction = blockTransactionService.findByCondition(condition);
        blockTransaction.forEach(obj -> {
            blockTransactionService.updateSuccess(obj);
        });
    }


    private void newListener() {
        web3j.pendingTransactionObservable().subscribe(
                tx -> pendingTransactionObservable(tx),
                err -> processError(err),
                () -> processDone("new")
        );
    }

    //fail 0x2c952ed7ffc21b061cf79951047ae04efbc0104317fe07ad58b92a196b65fd03
    private void pendingTransactionObservable(Transaction tx) {
        try {
            saveOrUpdate(blockTransaction(tx));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replayTransactionsObservable(Transaction tx) {
        try {
            BlockTransaction trans = blockTransaction(tx);
            if (null != trans) {
                saveOrUpdate(trans);
                BigDecimal fromValue = getBalance(trans.getFromAddress(), trans.getTokenId());
                BigDecimal toValue = getBalance(trans.getToAddress(), trans.getTokenId());
                updateAddressBalance(trans.getTokenId(), trans.getFromAddress(), fromValue);
                updateAddressBalance(trans.getTokenId(), trans.getToAddress(), toValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BigDecimal getBalance(String address, BigInteger tokenId) {
        BigDecimal balance = BigDecimal.ZERO;
        try {
            if (tokenId.equals(BusinessConstant.BASE_TOKEN_ID_ETH)) {
                BigInteger balanceWei = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
                balance = Convert.fromWei(new BigDecimal(balanceWei), Convert.Unit.ETHER);
            } else {
                CommonToken token = commonTokenService.findById(tokenId);
                if (null != token) {
                    BigInteger balanceWei = contractService.balanceOf(token.getTokenContractAddress(), address);
                    balance = new BigDecimal(balanceWei).divide(BigDecimal.TEN.pow(token.getTokenDecimal()), RoundingMode.HALF_DOWN);
                }
            }
            return balance;
        } catch (Exception e) {
            e.printStackTrace();
            return balance;
        }
    }

    private void processError(Throwable err) {
        err.printStackTrace();
    }

    private void processDone(String type) {

    }


    private void oldListener() {
        String lastNumber = getHeight();
        while (true) {
//            log.info("block listener is running, lastNumber is " + lastNumber);
            try {
                Thread.sleep(100);
                BigInteger height = web3j.ethBlockNumber().send().getBlockNumber();
                if (lastNumber.equals(String.valueOf(height))) {
                    continue;
                }
                //0x8e3c345eb9de5aa4fba102d7ab71b4a2ed7445e90fc392555c0de1f32b10df8e
                //单个区块监听,避免消耗过大
                DefaultBlockParameter start = DefaultBlockParameter.valueOf(NumberUtils.createBigInteger(lastNumber));
                web3j.replayTransactionsObservable(start, start).subscribe(
                        tx -> replayTransactionsObservable(tx),
                        err -> processError(err),
                        () -> processDone("old"));
                if (!lastNumber.equals(String.valueOf(height))) {
                    lastNumber = String.valueOf(height);
                    redisTemplate.opsForValue().set(RedisConstant.ETH_LAST_HEIGHT, lastNumber);
                    updateStatus(lastNumber);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @NotNull
    private String getHeight() {
        String lastNumber = redisTemplate.opsForValue().get(RedisConstant.ETH_LAST_HEIGHT);
        if (StringUtils.isBlank(lastNumber)) {
            BigInteger height = null;
            try {
                height = web3j.ethBlockNumber().send().getBlockNumber();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lastNumber = String.valueOf(height);
            redisTemplate.opsForValue().set(RedisConstant.ETH_LAST_HEIGHT, lastNumber);
        }
        return lastNumber;
    }

    private BlockTransaction blockTransaction(final Transaction tx) throws IOException {
        String from = tx.getFrom();
        String to = getAddressFromInput(tx);
        CommonAddress address = isOurAddress(from, to);
        //approve方法则仅修改状态值
        if (isApprove(tx)) {
            updateApprove(from);
            return null;
        }
        //非内部地址忽略
        if (null == address) {
            return null;
        }
        BlockTransaction transaction = new BlockTransaction();
        Optional<TransactionReceipt> receipt = web3j.ethGetTransactionReceipt(tx.getHash()).send().getTransactionReceipt();
        Long time = System.currentTimeMillis();
        transaction.setFromAddress(from);
        transaction.setToAddress(to);
        transaction.setHash(tx.getHash());
        transaction.setUpdatedAt(time);
        transaction.setTokenType("ETH");
        transaction.setCreatedAt(time);
        transaction.setValue(getValue(tx));
        transaction.setUserId(address.getUserId());
        transaction.setOrderNumber("");
        //根据地址判断操作类型
        if (address.getUserId().equals(BigInteger.ZERO)) {
            transaction.setOprType(9);
        } else if (to.equalsIgnoreCase(address.getAddress())) {
            transaction.setOprType(1);
        } else {
            transaction.setOprType(2);
        }
        if (isContractTransfer(tx)) {
            CommonToken token = getTokenContract(tx.getTo());
            if (null == token) {
                return null;
            }
            transaction.setTokenId(token.getId());
        } else {
            transaction.setTokenId(BusinessConstant.BASE_TOKEN_ID_ETH);
        }
        //判断记录内容， 该值只有在非打包阶段才会有
        if (null == receipt || receipt.isEmpty()) {
            transaction.setFee(BigDecimal.ZERO);
            transaction.setHeight(BigInteger.ZERO);
            transaction.setStatus(0);
            transaction.setTransactionStatus(4);
        } else if (isContractTransfer(tx) || isEthTransfer(tx)) {
            BigInteger gas = null;
            try {
                gas = tx.getGasPrice().multiply(receipt.get().getGasUsed());
            } catch (Exception e) {
                e.printStackTrace();
            }
            transaction.setFee(Convert.fromWei(new BigDecimal(gas), Convert.Unit.ETHER));
            transaction.setHeight(receipt.get().getBlockNumber());
            transaction.setStatus(1);
            transaction.setTransactionStatus(4);
            if (receipt.get().getStatus().equals("0x1") && receipt.get().getLogs().size() == 0) {
                transaction.setErrorMsg("转账失败");
                transaction.setErrorData("转账失败");
                transaction.setStatus(9);
                transaction.setTransactionStatus(6);
            }
        }
        return transaction;
    }

    private Boolean isContractTransfer(Transaction tx) {
        if (null == tx.getInput() || tx.getInput().equals("0x") || tx.getInput().length() <= 10) {
            return false;
        }
        String method = tx.getInput().substring(0, 10);
        return transferArr.contains(method) || transferFromArr.contains(method);
    }

    private Boolean isApprove(Transaction tx) {
        if (null == tx.getInput() || tx.getInput().equals("0x") || tx.getInput().length() <= 10) {
            return false;
        }
        String method = tx.getInput().substring(0, 10);
        return approveArr.contains(method);
    }

    private Boolean isEthTransfer(final Transaction tx) {
        if (null != tx.getInput() && tx.getInput().equals("0x")) {
            return true;
        }
        return false;
    }

    private CommonToken getTokenContract(String contractAddress) {
        CommonToken commonToken = tokenMap.get(contractAddress.toUpperCase());
        if (null == commonToken) {
            commonToken = commonTokenService.findOneBy("tokenContractAddress", contractAddress);
            if (null != commonToken) {
                tokenMap.put(contractAddress.toUpperCase(), commonToken);
            }
        }
        return commonToken;
    }

    private BigDecimal getValue(Transaction tx) {
        if (isEthTransfer(tx)) {
            return Convert.fromWei(new BigDecimal(tx.getValue()), Convert.Unit.ETHER);
        } else if (isContractTransfer(tx)) {
            CommonToken commonToken = getTokenContract(tx.getTo());
            if (null == commonToken) {
                commonToken = commonTokenService.findOneBy("tokenContractAddress", tx.getTo());
                return BigDecimal.ZERO;
            }
            String value = transferArr.contains(tx.getInput().substring(0, 10)) ? tx.getInput().substring(74) : tx.getInput().substring(138);
            Method refMethod = null;
            Uint256 amount = null;
            try {
                refMethod = TypeDecoder.class.getDeclaredMethod("decode", String.class, int.class, Class.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            refMethod.setAccessible(true);
            try {
                amount = (Uint256) refMethod.invoke(null, value, 0, Uint256.class);
            } catch (Exception e) {
                return BigDecimal.ZERO;
            }
            BigDecimal result = new BigDecimal(amount.getValue()).divide(BigDecimal.TEN.pow(commonToken.getTokenDecimal()), RoundingMode.HALF_DOWN);
            return result;
        } else {
            return BigDecimal.ZERO;
        }

    }

    private String getAddressFromInput(final Transaction tx) {
        if (isEthTransfer(tx)) {
            return tx.getTo();
        } else if (isContractTransfer(tx)) {
            String inputData = tx.getInput();
            String to = inputData.substring(10, 74);
            Method refMethod = null;
            try {
                refMethod = TypeDecoder.class.getDeclaredMethod("decode", String.class, int.class, Class.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            refMethod.setAccessible(true);
            Address address = null;
            try {
                address = (Address) refMethod.invoke(null, to, 0, Address.class);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return address.getValue();
        }
        return null;
    }

    @Override
    public BigInteger getNonce(Map<String, BigInteger> nonceMap, String address) throws IOException {
        BigInteger nonce = nonceMap.get(address);
        if (nonce == null) {
            nonce = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount();
            nonceMap.put(address, nonce);
        } else {
            nonce = nonce.add(BigInteger.ONE);
            nonceMap.put(address, nonce);
        }
        return nonce;
    }

    @Override
    public BigInteger getEthEstimateApprove(String contractAddress, String from, String to) throws IOException {
        if (StringUtils.isBlank(contractAddress)) {
            return BigInteger.valueOf(21000);
        }
        Uint256 limit = new Uint256(BigInteger.TEN.pow(18).multiply(MAX_APPROVE));
        Function function = new Function(
                "approve",
                Arrays.asList(new Address(to), limit),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        EthEstimateGas result = web3j.ethEstimateGas(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(from, contractAddress, encodedFunction)).send();
        if (result.getError() == null) {
            return result.getAmountUsed();
        } else {
            return null;
        }
    }

    @Override
    public BigInteger getEthEstimateTransferFrom(String contractAddress, String from, String to) throws IOException {
        Uint256 limit = new Uint256(MAX_APPROVE.multiply(BigInteger.TEN.pow(18)));
        Function function = new Function(
                "transferFrom",
                Arrays.asList(new Address(from), new Address(to), limit),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        EthEstimateGas result = web3j.ethEstimateGas(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(from, contractAddress, encodedFunction)).send();
        if (result.getError() == null) {
            return result.getAmountUsed();
        } else {
            return null;
        }
    }

    @Override
    public void send(AdminWallet hot, String address, BigDecimal fromWei) throws IOException {
        BigInteger nonce = web3j.ethGetTransactionCount(hot.getAddress(), DefaultBlockParameterName.PENDING).send().getTransactionCount();
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(hot.getPvKey()));
        Credentials ALICE = Credentials.create(ecKeyPair);
        RawTransaction transaction = RawTransaction.createEtherTransaction(nonce, Convert.toWei("5", Convert.Unit.GWEI).toBigInteger(), BigInteger.valueOf(21000), address, Convert.toWei(fromWei, Convert.Unit.ETHER).toBigInteger());
        byte[] signedMessage = TransactionEncoder.signMessage(transaction, ALICE);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction result = web3j.ethSendRawTransaction(hexValue).send();
    }

    @Override
    public BigDecimal getBalance(String tokenName) {
        AdminWallet cold = adminWalletService.getEthCold();
        CommonToken token = commonTokenService.findOneBy("tokenName", tokenName);
        if (null == token) {
            return BigDecimal.ZERO;
        }
        return getBalance(cold.getAddress(), token.getTokenContractAddress());
    }

    @Override
    public BigInteger getEthEstimateTransfer(String contractAddress, String toAddress, String from, BigDecimal value) throws IOException {
        Uint256 limit = new Uint256(value.toBigInteger());
        Function function = new Function(
                "transfer",
                Arrays.asList(new Address(toAddress), limit),
                Collections.singletonList(new TypeReference<Bool>() {
                }));
        String encodedFunction = FunctionEncoder.encode(function);
        EthEstimateGas result = web3j.ethEstimateGas(
                org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction(from, contractAddress, encodedFunction)).send();
        if (result.getError() == null) {
            return result.getAmountUsed();
        } else {
            return null;
        }
    }

    private BigDecimal getBalance(String address, String contractAddress) {
        BigDecimal result = null;
        BigInteger balance;
        try {
            if (StringUtils.isBlank(contractAddress)) {
                balance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
                result = Convert.fromWei(new BigDecimal(balance), Convert.Unit.ETHER);
            } else {
                balance = contractService.balanceOf(contractAddress, address);
                CommonToken contract = getTokenContract(contractAddress);
                if (null == contract) {
                    return BigDecimal.ZERO;
                }
                result = new BigDecimal(balance).divide(BigDecimal.TEN.pow(contract.getTokenDecimal()), RoundingMode.HALF_DOWN);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }


    }
}
