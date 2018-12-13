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
import com.neemre.btcdcli4j.core.domain.SignatureResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BtcAction {

    protected static ObjectMapper objectMapper = new ObjectMapper();
    protected static Integer propId;
    protected static BtcdClient btcdClient;

    public static void init(Integer id, BtcdClient client) {
        propId = id;
        btcdClient = client;
    }

    public static BigDecimal getBalance() throws BitcoindException, IOException, CommunicationException {
        return btcdClient.getBalance();
    }

    public static List<OmniWalletAddressBalance> getOmniBalance() throws BitcoindException, IOException, CommunicationException {
        Object result = btcdClient.remoteCall("omni_getwalletaddressbalances", Arrays.asList());
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

    public static String sendToAddress(String address, BigDecimal amount) throws BitcoindException, IOException, CommunicationException {
        return btcdClient.sendToAddress(address, amount);
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
    public static String prepareCollection(String tetherAddress, String toAddress) throws BitcoindException, IOException, CommunicationException {
        // 1.Get tether balance.
        TetherBalance tetherBalance = getTetherBalance(tetherAddress);

        // 2.Get tether address unspent.
        List<Output> unspents = listUnspent(Arrays.asList(tetherAddress));
        Output unspent = null;
        if (unspents.size() > 0) {
            unspent = unspents.get(0);
        } else {
            throw new RuntimeException("No unspent on address-" + tetherAddress + " found!");
        }

        // 3.Send how much tether.
        String simpleSendResult = objectMapper.readValue(btcdClient.remoteCall("omni_createpayload_simplesend",
                Arrays.asList(propId, tetherBalance.getBalance().toString())).toString(), String.class);

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
//        BigDecimal fee = new BigDecimal(config.get("tetherCollectFee"));
        BigDecimal fee = new BigDecimal(0);
        OmniCreaterawtxChangeRequiredEntity entity = new OmniCreaterawtxChangeRequiredEntity(unspent.getTxId(), unspent.getVOut(), unspent.getScriptPubKey(), fee);
        String changeResult = objectMapper.readValue(btcdClient.remoteCall("omni_createrawtx_change",
                Arrays.asList(referenceResult, Arrays.asList(entity), tetherAddress, fee)).toString(),
                String.class);

        System.out.println("#Tether Balance        : " + tetherBalance);
        System.out.println("#Unspent on Address    : " + unspent);
        System.out.println("#Simple Send Result    : " + simpleSendResult);
        System.out.println("#Raw Transaction Result: " + createRawTransactionResult);
        System.out.println("#Combined Result       : " + combinedResult);
        System.out.println("#Reference Result      : " + referenceResult);
        System.out.println("#Change Result         : " + changeResult);
        return changeResult;
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
