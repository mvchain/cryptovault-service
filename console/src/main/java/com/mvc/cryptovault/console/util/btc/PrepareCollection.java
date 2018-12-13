package com.mvc.cryptovault.console.util.btc;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

import java.io.IOException;

public class PrepareCollection extends BtcAction {

    private static String tetherAddress;
    private static String toAddress;

    public static void main(String[] args) throws BitcoindException, IOException, CommunicationException {
        parseArgs(args);
        prepareCollection(tetherAddress, toAddress);
    }

    private static void parseArgs(String[] args) {
        tetherAddress = args[0];
        toAddress = args[1];
    }
}
