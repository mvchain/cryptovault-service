package com.mvc.cryptovault.console.util.btc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvc.cryptovault.console.util.btc.entity.OmniCreaterawtxChangeRequiredEntity;
import com.mvc.cryptovault.console.util.btc.entity.OmniWalletAddressBalance;
import com.mvc.cryptovault.console.util.btc.entity.TetherBalance;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.Output;
import com.neemre.btcdcli4j.core.domain.OutputOverview;
import com.neemre.btcdcli4j.core.domain.SignatureResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class BtcAction {

    protected static ObjectMapper objectMapper = new ObjectMapper();
    public static Integer propId;
    protected static BtcdClient btcdClient;

    public static void init(Integer id, BtcdClient client) {
        propId = id;
        btcdClient = client;
    }

    public static BigDecimal getBalance() throws BitcoindException, IOException, CommunicationException {
        return btcdClient.getBalance();
    }

    public static List<OmniWalletAddressBalance> getOmniBalance() throws BitcoindException, IOException, CommunicationException {
        Object result = btcdClient.remoteCall("omni_getwalletaddressbalances", Arrays.asList(true));
        List<OmniWalletAddressBalance> omniBalanceList = objectMapper.readValue(result.toString(), new TypeReference<List<OmniWalletAddressBalance>>() {
        });
        return omniBalanceList;
    }

    public static TetherBalance getTetherBalance(String address) throws BitcoindException, IOException, CommunicationException {
        Object result = btcdClient.remoteCall("omni_getbalance", Arrays.asList(address, propId));
        TetherBalance tetherBalance = objectMapper.readValue(result.toString(), TetherBalance.class);
        tetherBalance.setAddress(address);
        return tetherBalance;
    }

    public static List<TetherBalance> getTetherBalance() throws BitcoindException, IOException, CommunicationException {
        List<OmniWalletAddressBalance> omniBalanceList = getOmniBalance();
        Integer tetherId = propId;
        List<TetherBalance> tetherBalanceList = new ArrayList<>();
        for (OmniWalletAddressBalance omniWalletAddressBalance : omniBalanceList) {
            TetherBalance tetherBalance = TetherBalance.convert(tetherId, omniWalletAddressBalance);
            if (tetherBalance != null) tetherBalanceList.add(tetherBalance);
        }
        return tetherBalanceList;
    }

    public static List<Output> listUnspent(List<String> addresses) throws BitcoindException, IOException, CommunicationException {
        if (addresses != null) {
            return btcdClient.listUnspent(0, 9999999, addresses);
        } else {
            return btcdClient.listUnspent();
        }
    }

    /**
     * 1.Get tether balance.
     * 2.Get tether address unspent.
     * 3.Send how much tether.
     * 4.Create BTC Raw Transaction.
     * 5.Add omni token(Tehter) data to the transaction.
     * 6.Add collect/to address.
     * 7.Add fee.
     *
     * @param tetherAddress
     * @return
     */
    public static String prepareCollection(String tetherAddress, String toAddress, BigDecimal fee, BigDecimal value) throws BitcoindException, IOException, CommunicationException {
        // 1.Get tether balance.
        TetherBalance tetherBalance = getTetherBalance(tetherAddress);

        // 2.Get tether address unspent.
        List<Output> unspents = listUnspent(Arrays.asList(tetherAddress));
        Output unspent = null;
        if (unspents.size() > 0) {
            unspents = unspents.stream().filter(obj -> obj.getAmount().compareTo(fee) >= 0).collect(Collectors.toList());
            if (unspents.size() == 0) {
                throw new RuntimeException("No unspent on address-" + tetherAddress + " found!");
            }
            unspent = unspents.get(0);
        } else {
            throw new RuntimeException("No unspent on address-" + tetherAddress + " found!");
        }

        // 3.Send how much tether.
        BigDecimal balance = null == value ? tetherBalance.getBalance() : value;
        String simpleSendResult = objectMapper.readValue(btcdClient.remoteCall("omni_createpayload_simplesend",
                Arrays.asList(propId, balance.toString())).toString(), String.class);

        // 4.Create BTC Raw Transaction.
        String createRawTransactionResult = btcdClient.createRawTransaction(Arrays.asList(unspent), new HashMap<>());

        // 5.Add omni token(Tehter) data to the transaction.
        String combinedResult = objectMapper.readValue(btcdClient.remoteCall("omni_createrawtx_opreturn",
                Arrays.asList(createRawTransactionResult.toString(), simpleSendResult)).toString(),
                String.class);

        // 6.Add collect/to address.
        String referenceResult = objectMapper.readValue(btcdClient.remoteCall("omni_createrawtx_reference",
                Arrays.asList(combinedResult, toAddress)).toString(),
                String.class);

        // 7.Add fee.
        OmniCreaterawtxChangeRequiredEntity entity = new OmniCreaterawtxChangeRequiredEntity(unspent.getTxId(), unspent.getVOut(), unspent.getScriptPubKey(), unspent.getAmount());
        String changeResult = objectMapper.readValue(btcdClient.remoteCall("omni_createrawtx_change",
                Arrays.asList(referenceResult, Arrays.asList(entity), tetherAddress, fee)).toString(),
                String.class);

//        System.out.println("#Tether Balance        : " + tetherBalance);
//        System.out.println("#Unspent on Address    : " + unspent);
//        System.out.println("#Simple Send Result    : " + simpleSendResult);
//        System.out.println("#Raw Transaction Result: " + createRawTransactionResult);
//        System.out.println("#Combined Result       : " + combinedResult);
//        System.out.println("#Reference Result      : " + referenceResult);
//        System.out.println("#Change Result         : " + changeResult);
        return changeResult;
    }

    /**
     * @param centerAddress 除了目标地址外的找零地址
     * @param fee           单个地址的手续费,目标地址为多个时累加计算
     * @param value         发送数量,仅支持相同数量的发送
     * @param addresses     发送的目标地址列表
     * @return
     */
    public static String sendToAddressWithRaw(String centerAddress, BigDecimal fee, BigDecimal value, List<String> addresses) throws BitcoindException, CommunicationException {
        if (fee.compareTo(BigDecimal.ZERO) == 0 || addresses.size() == 0) {
            throw new RuntimeException("Input Failed!");
        }
        BigDecimal total = btcdClient.getBalance();
        BigDecimal use = BigDecimal.ZERO;
        Map<String, BigDecimal> output = new HashMap<>(addresses.size());
        List<Output> listUnspent = btcdClient.listUnspent();
        listUnspent = listUnspent.stream().filter(obj -> obj.getSpendable() == true).collect(Collectors.toList());
        if (listUnspent.size() == 0) {
            //如果余额不足则直接
            return null;
        }
        List<OutputOverview> input = new ArrayList<>(addresses.size());
        for (Output obj : listUnspent) {
            //使用后余额也还原到该地址
            input.add(obj);
        }
        for (String address : addresses) {
            use = use.add(value);
            output.put(address, value);
        }
        //找零 = 总余额 - 发送余额 - 预设手续费
        output.put(centerAddress, total.subtract(use).subtract(fee));
        String row = btcdClient.createRawTransaction(input, output);
        SignatureResult res = btcdClient.signRawTransaction(row);
        if (res.getComplete()) {
            return btcdClient.sendRawTransaction(res.getHex());
        } else {
            throw new RuntimeException("Sign Raw Transaction Failed!");
        }
    }


    public static String sendToAddress(String address, BigDecimal amount) throws BitcoindException, IOException, CommunicationException {
        return btcdClient.sendToAddress(address, amount);
    }

    public static SignatureResult signRawTransaction(String rawTx) throws BitcoindException, IOException, CommunicationException {
        SignatureResult result = btcdClient.signRawTransaction(rawTx);
        if (result.getComplete()) {
            return result;
        } else {
            throw new RuntimeException("Sign Raw Transaction Failed!");
        }
    }

    public static String sendRawTransaction(String hex) throws BitcoindException, IOException, CommunicationException {
        return btcdClient.sendRawTransaction(hex);
    }
}
