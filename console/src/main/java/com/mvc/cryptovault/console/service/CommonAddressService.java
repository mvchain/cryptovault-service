package com.mvc.cryptovault.console.service;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.constant.BusinessConstant;
import com.mvc.cryptovault.console.dao.CommonAddressMapper;
import com.mvc.cryptovault.console.util.btc.BtcAction;
import com.mvc.cryptovault.console.util.btc.entity.BtcOutput;
import com.mvc.cryptovault.console.util.btc.entity.TetherBalance;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.domain.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    Web3j web3j;
    @Autowired
    BlockTransactionService blockTransactionService;

    public List<ExportOrders> exportSign() throws IOException {
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
        if (null == hot || null == cold) {
            return result;
        }
        for (BlockTransaction transaction : list) {
            if (transaction.getTokenType().equalsIgnoreCase("ETH")) {
                addEthWithdrawOrder(result, nonceMap, tokenMap, hot, cold, transaction);
            } else if (transaction.getTokenType().equalsIgnoreCase("BTC")) {
                addBtcWithdrawOrder(result, tokenMap, btcCold, transaction);
            }
        }
        return result;
    }

    private void addBtcWithdrawOrder(List<ExportOrders> result, Map<BigInteger, CommonToken> tokenMap, AdminWallet btcCold, BlockTransaction transaction) {
        try {
            CommonToken token = tokenMap.get(BusinessConstant.BASE_TOKEN_ID_USDT);
            List<Output> unspents = BtcAction.listUnspent(Arrays.asList(btcCold.getAddress()));
            if (unspents.size() <= 0) return;
            List<BtcOutput> btcOutputs = new ArrayList<>(unspents.size());
            unspents.forEach(obj-> btcOutputs.add(new BtcOutput(obj)));
            String str = JSON.toJSONString(btcOutputs);
            ExportOrders orders = new ExportOrders();
            orders.setFromAddress(btcCold.getAddress());
            orders.setTokenType(token.getTokenType());
            //实际转账金额需要扣除手续费
            orders.setValue(transaction.getValue().subtract(BigDecimal.valueOf(token.getTransaferFee())));
            orders.setToAddress(transaction.getToAddress());
            orders.setGasPrice(new BigDecimal(String.valueOf(token.getTransaferFee())));
            orders.setOrderId(transaction.getOrderNumber());
            orders.setContractAddress(str);
            orders.setOprType(1);
            result.add(orders);
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
        BigInteger gasLimit = blockService.get("ETH").getEthEstimateTransfer(token.getTokenContractAddress(), transaction.getToAddress(), cold.getAddress(), value);

        if (token.getId().equals(BusinessConstant.BASE_TOKEN_ID_ETH)) {
            //实际转账金额需要扣除手续费
            BigDecimal fee = Convert.fromWei(new BigDecimal(gasLimit.multiply(gasPrice)), Convert.Unit.ETHER);
            value = value.subtract(fee);
        } else {
            //erc20需要扣除预设的手续费(实际手续费+浮动手续费,实际手续费必须存在)
            Float fee = null == token.getFee() ? token.getTransaferFee() : token.getTransaferFee() + token.getFee();
            value = value.subtract(BigDecimal.valueOf(fee));
        }
        orders.setFromAddress(cold.getAddress());
        orders.setTokenType(token.getTokenType());
        orders.setValue(value);
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

    private void addUsdtOrder(List<ExportOrders> result) throws BitcoindException, IOException, CommunicationException {
        List<TetherBalance> list = BtcAction.getTetherBalance();
        CommonToken token = commonTokenService.findById(BusinessConstant.BASE_TOKEN_ID_USDT);
        AdminWallet coldBtc = adminWalletService.getBtcCold();
        if (null == list) return;
        for (TetherBalance tetherAddress : list) {
            ExportOrders orders = new ExportOrders();
            List<Output> unspents = BtcAction.listUnspent(Arrays.asList(tetherAddress.getAddress()));
            if (unspents.size() <= 0) return;
            List<BtcOutput> btcOutputs = new ArrayList<>(unspents.size());
            unspents.forEach(obj-> btcOutputs.add(new BtcOutput(obj)));
            String str = JSON.toJSONString(btcOutputs);
            orders.setGasPrice(NumberUtils.parseNumber(String.valueOf(token.getTransaferFee()), BigDecimal.class));
            orders.setOprType(0);
            orders.setToAddress(coldBtc.getAddress());
            orders.setValue(tetherAddress.getBalance());
            orders.setTokenType("BTC");
            orders.setContractAddress(str);
            orders.setFromAddress(tetherAddress.getAddress());
            result.add(orders);
        }
    }

    private void addEthOrder(List<ExportOrders> result, Map<String, BigInteger> nonceMap, AdminWallet hot, AdminWallet cold, CommonAddress address, ExportOrders orders, CommonToken token) throws IOException {
        BigInteger nonce;
        BigInteger gasPrice = Convert.toWei(new BigDecimal(token.getTransaferFee()), Convert.Unit.GWEI).toBigInteger();
        //erc20地址需要先运行approve方法
        if (address.getApprove() == 0 && address.getTokenType().equalsIgnoreCase("ETH") && !address.getAddressType().equalsIgnoreCase("ETH")) {
            nonce = getNonce(nonceMap, address.getAddress());
            BigInteger gasLimit = blockService.get("ETH").getEthEstimateApprove(token.getTokenContractAddress(), address.getAddress(), cold.getAddress());
            //预先发送手续费,该操作gasPrice暂时固定
            BigDecimal value = Convert.fromWei(new BigDecimal(gasLimit.multiply(gasPrice)), Convert.Unit.ETHER);
            if (web3j.ethGetBalance(address.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance().compareTo(gasLimit.multiply(gasPrice)) < 0) {
                blockService.get("ETH").send(hot, address.getAddress(), value);
            }
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
        //transfer from,由中心账户发起并支付手续费
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
        result.add(orders);
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
}