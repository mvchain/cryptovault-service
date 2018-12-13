package com.mvc.cryptovault.console.util.btc.entity;

import java.math.BigDecimal;

public class TetherBalance {
    private String address;
    private BigDecimal balance;
    private BigDecimal reserved;
    private BigDecimal frozen;

    public static TetherBalance convert(int tetherId, OmniWalletAddressBalance omniWalletAddressBalance){
        TetherBalance tetherBalance = null;
        for (OmniBalance omniBalance : omniWalletAddressBalance.getBalances()) {
            if (tetherId == omniBalance.getPropertyid()) {
                tetherBalance = new TetherBalance();
                tetherBalance.setAddress(omniWalletAddressBalance.getAddress());
                tetherBalance.setBalance(omniBalance.getBalance());
                tetherBalance.setFrozen(omniBalance.getFrozen());
                tetherBalance.setReserved(omniBalance.getReserved());
                break;
            }
        }
        return tetherBalance;
    }

    @Override
    public String toString() {
        return "TetherBalance{" +
                "address='" + address + '\'' +
                ", balance=" + balance +
                ", reserved=" + reserved +
                ", frozen=" + frozen +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getReserved() {
        return reserved;
    }

    public void setReserved(BigDecimal reserved) {
        this.reserved = reserved;
    }

    public BigDecimal getFrozen() {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen) {
        this.frozen = frozen;
    }
}
