package com.mvc.cryptovault.console.util.btc;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

import java.io.IOException;
import java.math.BigDecimal;

// Tether: mv9YDCjtqeJ6LaNyBtk21ALWkrpnm2RuKb
// Center: mtgK7jYoY9n2gLAUmsK4M52YD5LLK1r7L
public class SendToAddress extends BtcAction {
    private static String to;
    private static String amount;

    public static void main(String[] args) throws BitcoindException, IOException, CommunicationException {
        parseArgs(args);
        System.out.println(sendToAddress(to, new BigDecimal(amount)));
    }

    private static void parseArgs(String[] args) {
        to = args[0];
        amount = args[1];
    }
}
