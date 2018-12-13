package com.mvc.cryptovault.console.util.btc;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

import java.io.IOException;

public class SignRawTransaction extends BtcAction {

    private static String rawTx;

    public static void main(String[] args) throws BitcoindException, IOException, CommunicationException {
        parseArgs(args);
        System.out.println(signRawTransaction(rawTx));
    }

    private static void parseArgs(String[] args) {
        rawTx = args[0];
    }
}
