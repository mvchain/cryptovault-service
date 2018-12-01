package com.mvc.cryptovault.console.service;

import com.mvc.cryptovault.common.bean.*;
import com.mvc.cryptovault.console.common.AbstractService;
import com.mvc.cryptovault.console.common.BaseService;
import com.mvc.cryptovault.console.dao.CommonAddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        if (null == hot || null == cold) {
            return result;
        }
        for (BlockTransaction transaction : list) {
            addEthWithdrawOrder(result, nonceMap, tokenMap, hot, cold, transaction);
        }
        return result;
    }


    private void addEthWithdrawOrder(List<ExportOrders> result, Map<String, BigInteger> nonceMap, Map<BigInteger, CommonToken> tokenMap, AdminWallet hot, AdminWallet cold, BlockTransaction transaction) throws IOException {
        ExportOrders orders = new ExportOrders();
        CommonToken token = tokenMap.get(transaction.getTokenId());
        BigInteger gasPrice = Convert.toWei(new BigDecimal(token.getTransaferFee()), Convert.Unit.GWEI).toBigInteger();
        BigInteger nonce = getNonce(nonceMap, cold.getAddress());
        BigDecimal value = transaction.getValue().multiply(BigDecimal.TEN.pow(token.getTokenDecimal()));
        BigInteger gasLimit = blockService.getEthEstimateTransfer(token.getTokenContractAddress(), transaction.getToAddress(), cold.getAddress(), value);
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

    public List<ExportOrders> exportCollect() throws IOException {
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
            } else {
                addUsdtOrder(result, nonceMap, hot, cold, address, orders, token);
            }
        }
        return result;
    }

    private void addUsdtOrder(List<ExportOrders> result, Map<String, BigInteger> nonceMap, AdminWallet hot, AdminWallet cold, CommonAddress address, ExportOrders orders, CommonToken token) {
    }

    private void addEthOrder(List<ExportOrders> result, Map<String, BigInteger> nonceMap, AdminWallet hot, AdminWallet cold, CommonAddress address, ExportOrders orders, CommonToken token) throws IOException {
        BigInteger nonce;
        BigInteger gasPrice = Convert.toWei(new BigDecimal(token.getTransaferFee()), Convert.Unit.GWEI).toBigInteger();
        //erc20地址需要先运行approve方法
        if (address.getApprove() == 0 && address.getTokenType().equalsIgnoreCase("ETH") && !address.getAddressType().equalsIgnoreCase("ETH")) {
            nonce = getNonce(nonceMap, address.getAddress());
            BigInteger gasLimit = blockService.getEthEstimateApprove(token.getTokenContractAddress(), address.getAddress(), cold.getAddress());
            //预先发送手续费,该操作gasPrice暂时固定
            BigDecimal value = Convert.fromWei(new BigDecimal(gasLimit.multiply(gasPrice)), Convert.Unit.ETHER);
            if (web3j.ethGetBalance(address.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance().compareTo(gasLimit.multiply(gasPrice)) < 0) {
                blockService.send(hot, address.getAddress(), value);
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
        return blockService.getNonce(nonceMap, address);
    }

    public BigDecimal getBalance(String tokenName) {
        return blockService.getBalance(tokenName);
    }
}