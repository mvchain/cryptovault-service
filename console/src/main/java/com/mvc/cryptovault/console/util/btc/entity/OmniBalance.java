package com.mvc.cryptovault.console.util.btc.entity;

import java.math.BigDecimal;

public class OmniBalance {
    private Integer propertyid;
    private String name;
    private BigDecimal balance;
    private BigDecimal reserved;
    private BigDecimal frozen;

    @Override
    public String toString() {
        return "OmniBalance{" +
                "propertyid=" + propertyid +
                ", name='" + name + '\'' +
                ", balance=" + balance +
                ", reserved=" + reserved +
                ", frozen=" + frozen +
                '}';
    }

    public Integer getPropertyid() {
        return propertyid;
    }

    public void setPropertyid(Integer propertyid) {
        this.propertyid = propertyid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
