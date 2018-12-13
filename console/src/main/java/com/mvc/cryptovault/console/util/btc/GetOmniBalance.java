package com.mvc.cryptovault.console.util.btc;

import com.mvc.cryptovault.console.util.btc.entity.OmniWalletAddressBalance;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;

import java.io.IOException;
import java.util.List;

public class GetOmniBalance extends BtcAction {
    public static void main(String[] args) throws BitcoindException, IOException, CommunicationException {
        List<OmniWalletAddressBalance> omniBalanceList = getOmniBalance();

        // Print
        for (OmniWalletAddressBalance omniBalance : omniBalanceList) {
            System.out.println(omniBalance);
        }
    }
}
