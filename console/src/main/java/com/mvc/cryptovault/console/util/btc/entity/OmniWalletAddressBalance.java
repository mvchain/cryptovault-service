package com.mvc.cryptovault.console.util.btc.entity;

import java.util.List;

public class OmniWalletAddressBalance {

    private String address;
    private List<OmniBalance> balances;

    @Override
    public String toString() {
        return "OmniWalletAddressBalance{" +
                "address='" + address + '\'' +
                ", balances=" + balances +
                '}';
    }

    public List<OmniBalance> getBalances() {
        return balances;
    }

    public void setBalances(List<OmniBalance> balances) {
        this.balances = balances;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
