package com.mvc.cryptovault.console.service;

import com.github.pagehelper.PageHelper;
import com.mvc.cryptovault.common.bean.BlockTransaction;
import com.mvc.cryptovault.common.bean.CommonAddress;
import com.mvc.cryptovault.common.bean.CommonToken;
import com.mvc.cryptovault.common.constant.RedisConstant;
import com.mvc.cryptovault.common.util.BaseContextHandler;
import com.mvc.cryptovault.common.util.ConditionUtil;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.abi.TypeDecoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qiyichen
 * @create 2018/11/29 14:03
 */
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class EthService extends BlockService {

    @Autowired
    Web3j web3j;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    ContractService contractService;
    /**
     * input存在内容时, 只有转账方法需要记录,其他如创建合约之类的不需要记录
     * TODO 测试网和正是王方法id不同, transfer和transfer from 都需要记录
     */
    private List<String> transferArr = Arrays.asList(new String[]{"0xa9059cbb"});
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
                updateAddressBalance(trans.getFromAddress(), fromValue);
                updateAddressBalance(trans.getToAddress(), toValue);
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
                    balance = new BigDecimal(balanceWei).divide(BigDecimal.TEN.pow(token.getTokenDecimal()));
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
        System.out.println("process down: " + type);
    }


    private void oldListener() {
        String lastNumber = getHeight();
        while (true) {
            try {
                Thread.sleep(10);
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
            if (receipt.get().getStatus().equals("0x0")) {
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
        return transferArr.contains(method);
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
            String value = tx.getInput().substring(74);
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
            BigDecimal result = new BigDecimal(amount.getValue()).divide(BigDecimal.TEN.pow(commonToken.getTokenDecimal()));
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
}
