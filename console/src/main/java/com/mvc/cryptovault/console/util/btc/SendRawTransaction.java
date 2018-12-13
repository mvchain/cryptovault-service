package com.mvc.cryptovault.console.util.btc;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

import java.io.IOException;

public class SendRawTransaction extends BtcAction {

    private static String hex;

    public static void main(String[] args) throws BitcoindException, IOException, CommunicationException {
        parseArgs(args);
        System.out.println(sendRawTransaction(hex));
    }

    private static void parseArgs(String[] args) {
        hex = args[0];
    }
}
