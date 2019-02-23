package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.common.bean.vo.SignSumVO;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.CommonAddressMapper;
import com.mvc.cryptovault.console.util.btc.BtcAction;
import com.mvc.cryptovault.console.util.btc.entity.TetherBalance;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.Output;
import com.neemre.btcdcli4j.core.domain.OutputOverview;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class CommonAddressService extends AbstractService<CommonAddress> implements BaseService<CommonAddress> {

    @Autowired
    CommonAddressMapper commonAddressMapper;
    @Autowired
    CommonTokenService commonTokenService;
    @Autowired
    BlockService blockService;
    @Autowired
    AdminWalletService adminWalletService;
    @Autowired
    BlockHotAddressService blockHotAddressService;
    @Autowired
    Web3j web3j;
    @Autowired
    BtcdClient btcdClient;
    @Autowired
    BlockTransactionService blockTransactionService;
    @Autowired
    BlockUsdtWithdrawQueueService blockUsdtWithdrawQueueService;
    private final BigDecimal USDT_LIMIT_FEE = new BigDecimal("0.00000546");

    public List<SignSumVO> getExportSignSum() throws BitcoindException, IOException, CommunicationException {
        return blockTransactionService.exportSignSum();
    }

    public List<ExportOrders> exportSign() throws IOException, BitcoindException, CommunicationException {
        List<BlockTransaction> list = blockTransactionService.getSign();
        List<ExportOrders> result = new ArrayList<>(list.size());
        Map<String, BigInteger> nonceMap = new HashMap<>(list.size());
        Map<BigInteger, CommonToken> tokenMap = new ConcurrentHashMap<>();
        List<CommonToken> tokens = commonTokenService.findAll();
        for (CommonToken commonToken : tokens) {
            tokenMap.put(commonToken.getId(), commonToken);
        }
        AdminWallet hot = adminWalletService.getEthHot();
        AdminWallet cold = adminWalletService.getEthCold();
        AdminWallet btcCold = adminWalletService.getBtcCold();
        AdminWallet btcHot = adminWalletService.getBtcHot();
        if (null == hot || null == cold) {
            return result;
        }
        ExportOrders usdtOrder = new ExportOrders();
        BlockHotAddress blockHotAddress = new BlockHotAddress();
        Integer btcNumber = 0;
        StringBuilder orders = new StringBuilder();
        Map<String, BigDecimal> output = new HashMap<>(list.size());
        for (BlockTransaction transaction : list) {
            if (transaction.getTokenType().equalsIgnoreCase("ETH")) {
                addEthWithdrawOrder(result, nonceMap, tokenMap, hot, cold, transaction);
            } else if (transaction.getTokenType().equalsIgnoreCase("BTC")) {
                addBtcWithdrawOrder(orders, output, result, tokenMap, btcCold, transaction, usdtOrder, blockHotAddress, btcNumber);
            }
        }
        return result;
    }

    private void addBtcOrder(List<ExportOrders> result, Map<BigInteger, CommonToken> tokenMap, AdminWallet btcCold, StringBuilder orders, Map<String, BigDecimal> output) throws BitcoindException, IOException, CommunicationException {
        if (output.size() > 0) {
            List<Output> unspents = BtcAction.listUnspent(Arrays.asList(btcCold.getAddress()));
            Assert.isTrue(unspents.size() > 0, "冷钱包余额不足,请充值或等待确认");
            if (unspents.size() > 0) {
                BigDecimal balance = unspents.stream().map(obj -> obj.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal use = output.values().stream().map(obj -> obj).reduce(BigDecimal.ZERO, BigDecimal::add);

                List<OutputOverview> input = new ArrayList<>(unspents.size());
                for (Output obj : unspents) {
                    //使用后余额也还原到该地址
                    input.add(obj);
                }
                CommonToken token = tokenMap.get(BusinessConstant.BASE_TOKEN_ID_BTC);
                BigDecimal fee = NumberUtils.parseNumber(String.valueOf(token.getTransaferFee()), BigDecimal.class);
                output.put(btcCold.getAddress(), balance.subtract(BigDecimal.valueOf(fee.floatValue() * output.size())).subtract(BigDecimal.valueOf(use.floatValue())));
                String row = btcdClient.createRawTransaction(input, output);
                ExportOrders order = new ExportOrders();
                order.setFromAddress(btcCold.getAddress());
                order.setTokenType(token.getTokenType());
                order.setToAddress("");
                order.setGasLimit(BigDecimal.ZERO);
                order.setGasPrice(BigDecimal.ZERO);
                order.setOrderId(orders.toString());
                order.setNonce(null);
                order.setFeeAddress(JSON.toJSONString(unspents));
                order.setContractAddress(row);
                order.setOprType(1);
                result.add(order);
            }
        }
    }

    /**
     * USDT由热钱包中转,只记录单条记录
     *
     * @param result
     * @param tokenMap
     * @param btcCold
     * @param transaction
     */
    private void addBtcWithdrawOrder(StringBuilder orders, Map<String, BigDecimal> output, List<ExportOrders> result, Map<BigInteger, CommonToken> tokenMap, AdminWallet btcCold, BlockTransaction transaction, ExportOrders usdtOrder, BlockHotAddress blockHotAddress, Integer btcNumber) {
        try {
            CommonToken token = tokenMap.get(transaction.getTokenId());
            BigDecimal fee = new BigDecimal(String.valueOf(token.getTransaferFee()));
            ExportOrders order = new ExportOrders();
            order.setFeeAddress(transaction.getTokenId().equals(BusinessConstant.BASE_TOKEN_ID_BTC) ? null : String.valueOf(BtcAction.propId));
            order.setToAddress(transaction.getToAddress());
            order.setFromAddress(btcCold.getAddress());
            order.setValue(transaction.getValue());
            order.setOrderId(transaction.getOrderNumber());
            order.setOprType(1);
            order.setGasPrice(fee);
            order.setTokenType(token.getTokenType());
            order.setContractAddress(JSON.toJSONString(BtcAction.listUnspent(Arrays.asList(btcCold.getAddress()))));
            result.add(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEthWithdrawOrder(List<ExportOrders> result, Map<String, BigInteger> nonceMap, Map<BigInteger, CommonToken> tokenMap, AdminWallet hot, AdminWallet cold, BlockTransaction transaction) throws IOException {
        ExportOrders orders = new ExportOrders();
        CommonToken token = tokenMap.get(transaction.getTokenId());
        BigInteger gasPrice = Convert.toWei(new BigDecimal(token.getTransaferFee()), Convert.Unit.GWEI).toBigInteger();
        BigInteger nonce = getNonce(nonceMap, cold.getAddress());
        BigDecimal value = transaction.getValue().multiply(BigDecimal.TEN.pow(token.getTokenDecimal()));
        BigInteger gasLimit = null;
        if (token.getId().equals(BusinessConstant.BASE_TOKEN_ID_ETH)) {
            gasLimit = BigInteger.valueOf(21000);
            //实际转账金额需要扣除手续费
            orders.setValue(transaction.getValue());
        } else {
            //erc20暂时无法扣除手续费
            gasLimit = blockService.get("ETH").getEthEstimateTransfer(token.getTokenContractAddress(), transaction.getToAddress(), cold.getAddress(), value).multiply(BigInteger.valueOf(2));
            orders.setValue(value);
        }
        orders.setFromAddress(cold.getAddress());
        orders.setTokenType(token.getTokenType());
        orders.setToAddress(transaction.getToAddress());
        orders.setGasLimit(new BigDecimal(gasLimit));
        orders.setGasPrice(new BigDecimal(gasPrice));
        orders.setOrderId(transaction.getOrderNumber());
        orders.setNonce(nonce);
        orders.setContractAddress(token.getTokenContractAddress());
        orders.setOprType(1);
        result.add(orders);
    }

    public List<ExportOrders> exportCollect() throws IOException, BitcoindException, CommunicationException {
        List<CommonAddress> list = commonAddressMapper.findColldect();
        List<ExportOrders> result = new ArrayList<>(list.size());
        Map<String, BigInteger> nonceMap = new HashMap<>(list.size());
        Map<String, CommonToken> tokenMap = getTokenMap();
        AdminWallet hot = adminWalletService.getEthHot();
        AdminWallet cold = adminWalletService.getEthCold();
        if (null == hot || null == cold) {
            return result;
        }
        for (CommonAddress address : list) {
            ExportOrders orders = new ExportOrders();
            BigInteger nonce = null;
            CommonToken token = tokenMap.get(address.getAddressType().toUpperCase());
            if (null == token) {
                //不存在的令牌忽略
                continue;
            }
            if (address.getTokenType().equalsIgnoreCase("ETH")) {
                addEthOrder(result, nonceMap, hot, cold, address, orders, token);
            }
        }
        //添加需要汇总的usdt数据
        addUsdtOrder(result);
        return result;
    }

    private Boolean isCommonAddress(String address, AdminWallet coldBtc) {
        CommonAddress commonAddress = findOneBy("address", address);
        if (null != commonAddress && !address.equalsIgnoreCase(coldBtc.getAddress())) {
            //不是本系统中的地址或者地址为中心冷钱包地址则不需要汇总
            return true;
        }
        return false;
    }

    private Boolean isCommonAddress(String address) {
        CommonAddress commonAddress = findOneBy("address", address);
        if (null != commonAddress) {
            //不是本系统中的地址或者地址为中心冷钱包地址则不需要汇总
            return true;
        }
        return false;
    }

    private void addUsdtOrder(List<ExportOrders> result) throws BitcoindException, IOException, CommunicationException {
        CommonToken token = commonTokenService.findById(BusinessConstant.BASE_TOKEN_ID_USDT);
        BigDecimal fee = NumberUtils.parseNumber(String.valueOf(token.getTransaferFee()), BigDecimal.class);
        AdminWallet coldBtc = adminWalletService.getBtcCold();
        BigDecimal btcBalance = BtcAction.getBtcBalance(coldBtc.getAddress());
        BigDecimal usdtBalance = BtcAction.getTetherBalance(coldBtc.getAddress()).getBalance();
        //add btc collect
        List<Output> btcUnspents = btcdClient.listUnspent(1);
        btcUnspents = btcUnspents.stream().filter(obj -> isCommonAddress(obj.getAddress())).collect(Collectors.toList());
        ExportOrders btcOrders = new ExportOrders();
        btcOrders.setFromAddress(coldBtc.getAddress());
        btcOrders.setContractAddress(JSON.toJSONString(btcUnspents));
        btcOrders.setTokenType("BTC");
        btcOrders.setGasPrice(fee);
        btcOrders.setOprType(0);
        btcOrders.setValue(fee);
        btcOrders.setToAddress(BtcAction.getScriptPubKey(coldBtc.getAddress()));
        btcOrders.setNonce(BigInteger.ZERO);
        result.add(btcOrders);

        //add fee trans
        List<Output> feeUnspent = BtcAction.listUnspent(coldBtc.getAddress());
        List<TetherBalance> usdt = BtcAction.getTetherBalance();
        List<String> feeToAddress = new ArrayList<>();
        for (TetherBalance tetherBalance : usdt) {
            if (isCommonAddress(tetherBalance.getAddress(), coldBtc)) {
                feeToAddress.add(tetherBalance.getAddress());
            }
        }
        ExportOrders feeOrder = new ExportOrders();
        feeOrder.setFromAddress(coldBtc.getAddress());
        feeOrder.setContractAddress(JSON.toJSONString(feeUnspent));
        feeOrder.setTokenType("BTC");
        feeOrder.setGasPrice(fee);
        feeOrder.setOprType(0);
        feeOrder.setToAddress(JSON.toJSONString(feeToAddress));
        feeOrder.setValue(fee);
        feeOrder.setNonce(BigInteger.ONE);
        result.add(feeOrder);
        //add usdt trans
        for (TetherBalance tetherBalance : usdt) {
            if (!isCommonAddress(tetherBalance.getAddress(), coldBtc)) {
                continue;
            }
            ExportOrders orders = new ExportOrders();
            List<Output> unspents = BtcAction.listUnspent(tetherBalance.getAddress());
            String str = JSON.toJSONString(unspents);
            orders.setGasPrice(fee);
            orders.setOprType(0);
            orders.setToAddress(coldBtc.getAddress());
            orders.setValue(tetherBalance.getBalance());
            orders.setTokenType("BTC");
            orders.setContractAddress(str);
            orders.setFeeAddress(BtcAction.propId.toString());
            orders.setFromAddress(tetherBalance.getAddress());
            orders.setNonce(BigInteger.TWO);
            result.add(orders);
        }
    }

    private void addEthOrder(List<ExportOrders> result, Map<String, BigInteger> nonceMap, AdminWallet hot, AdminWallet cold, CommonAddress address, ExportOrders orders, CommonToken token) throws IOException {
        BigInteger nonce;
        BigInteger gasPrice = Convert.toWei(new BigDecimal(token.getTransaferFee()), Convert.Unit.GWEI).toBigInteger();
        //erc20地址需要先运行approve方法
        if (address.getApprove() == 0 && address.getTokenType().equalsIgnoreCase("ETH") && !address.getAddressType().equalsIgnoreCase("ETH")) {
            BigInteger gasLimit = blockService.get("ETH").getEthEstimateApprove(token.getTokenContractAddress(), address.getAddress(), cold.getAddress());
            //预先发送手续费,该操作gasPrice暂时固定
            BigDecimal value = Convert.fromWei(new BigDecimal(gasLimit.multiply(gasPrice)), Convert.Unit.ETHER);
            if (web3j.ethGetBalance(address.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance().compareTo(gasLimit.multiply(gasPrice)) < 0) {
                blockService.get("ETH").send(hot, address.getAddress(), value);
            }
            nonce = getNonce(nonceMap, address.getAddress());
            orders.setFromAddress(address.getAddress());
            orders.setTokenType(address.getTokenType());
            orders.setValue(address.getBalance());
            orders.setFeeAddress(cold.getAddress());
            orders.setGasLimit(new BigDecimal(gasLimit));
            orders.setGasPrice(new BigDecimal(gasPrice));
            orders.setOrderId(null);
            orders.setNonce(nonce);
            orders.setOprType(2);
            orders.setContractAddress(token.getTokenContractAddress());
            result.add(orders);
        }
        if (token.getId().equals(BusinessConstant.BASE_TOKEN_ID_ETH)) {
            orders = getEthExportOrders(nonceMap, cold, address, token, gasPrice);
        } else {
            //transfer from,由中心账户发起并支付手续费
            orders = getErc20ExportOrders(nonceMap, cold, address, token, gasPrice);
        }
        result.add(orders);
    }

    @NotNull
    private ExportOrders getEthExportOrders(Map<String, BigInteger> nonceMap, AdminWallet cold, CommonAddress address, CommonToken token, BigInteger gasPrice) throws IOException {
        BigInteger nonce;
        ExportOrders orders;
        nonce = getNonce(nonceMap, address.getAddress());
        orders = new ExportOrders();
        orders.setFromAddress(address.getAddress());
        orders.setTokenType(address.getTokenType());
        BigDecimal fee = new BigDecimal("21000").multiply(new BigDecimal(gasPrice)).divide(BigDecimal.TEN.pow(18));
        orders.setValue(address.getBalance().subtract(fee));
        orders.setToAddress(cold.getAddress());
        orders.setGasLimit(new BigDecimal("21000"));
        orders.setGasPrice(new BigDecimal(gasPrice));
        orders.setOrderId(null);
        orders.setNonce(nonce);
        orders.setContractAddress(token.getTokenContractAddress());
        orders.setOprType(0);
        return orders;
    }

    @NotNull
    private ExportOrders getErc20ExportOrders(Map<String, BigInteger> nonceMap, AdminWallet cold, CommonAddress address, CommonToken token, BigInteger gasPrice) throws IOException {
        BigInteger nonce;
        ExportOrders orders;
        BigDecimal value = address.getBalance().multiply(BigDecimal.TEN.pow(token.getTokenDecimal()));
        nonce = getNonce(nonceMap, cold.getAddress());
        orders = new ExportOrders();
        orders.setFromAddress(cold.getAddress());
        orders.setTokenType(address.getTokenType());
        orders.setValue(value);
        orders.setToAddress(address.getAddress());
        orders.setFeeAddress(cold.getAddress());
        orders.setGasLimit(new BigDecimal(80000));
        orders.setGasPrice(new BigDecimal(gasPrice));
        orders.setOrderId(null);
        orders.setNonce(nonce);
        orders.setContractAddress(token.getTokenContractAddress());
        orders.setOprType(0);
        return orders;
    }

    private Map<String, CommonToken> getTokenMap() {
        Map<String, CommonToken> map = new ConcurrentHashMap<>();
        List<CommonToken> list = commonTokenService.findAll();
        for (CommonToken commonToken : list) {
            map.put(commonToken.getTokenName().toUpperCase(), commonToken);
        }
        return map;
    }

    private BigInteger getNonce(Map<String, BigInteger> nonceMap, String address) throws IOException {
        return blockService.get("ETH").getNonce(nonceMap, address);
    }

    public BigDecimal getBalance(String tokenName) {
        return blockService.get("ETH").getBalance(tokenName);
    }

    public BigDecimal getWait(String tokenName) {
        BigDecimal value = commonAddressMapper.getWait(tokenName);
        return value;
    }
}