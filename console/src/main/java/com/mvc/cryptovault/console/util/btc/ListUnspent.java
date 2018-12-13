package com.mvc.cryptovault.console.util.btc;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.domain.Output;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// mv9YDCjtqeJ6LaNyBtk21ALWkrpnm2RuKb
// Output(super=OutputOverview(super=Entity(), txId=02feb8307c235f5436153b69967a3491a5f3f1ee3aea32d034bf4a53adf7874a, vOut=0),
// address=mv9YDCjtqeJ6LaNyBtk21ALWkrpnm2RuKb, account=mv9YDCjtqeJ6LaNyBtk21ALWkrpnm2RuKb, scriptPubKey=76a914a07d0c2c76f68471b2f2dd3f8a63aad062769b7488ac,
// redeemScript=null, amount=0.00006000, confirmations=0, spendable=true)
public class ListUnspent extends BtcAction {

    private static List<String> addresses;

    public static void main(String[] args) throws BitcoindException, IOException, CommunicationException {
        parseArgs(args);
        List<Output> unspentList = listUnspent(addresses);
        for (Output unspent : unspentList) {
            System.out.println(unspent);
        }
    }

    private static void parseArgs(String[] args) {
        if (args.length > 0) {
            addresses = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                addresses.add(args[i]);
            }
        }
    }
}
