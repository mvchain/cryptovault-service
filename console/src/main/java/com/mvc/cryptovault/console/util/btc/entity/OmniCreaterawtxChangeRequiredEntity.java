package com.mvc.cryptovault.console.util.btc.entity;

import java.math.BigDecimal;

public class OmniCreaterawtxChangeRequiredEntity {
    private String txid;
    private Integer vout;
    private String scriptPubKey;
    private BigDecimal value;

    public OmniCreaterawtxChangeRequiredEntity(String txid, Integer vout, String scriptPubKey, BigDecimal value) {
        this.txid = txid;
        this.vout = vout;
        this.scriptPubKey = scriptPubKey;
        this.value = value;
    }

    @Override
    public String toString() {
        return "OmniCreaterawtxChangeRequiredEntity{" +
                "txid='" + txid + '\'' +
                ", vout=" + vout +
                ", scriptPubKey='" + scriptPubKey + '\'' +
                ", value=" + value +
                '}';
    }

    public String getTxid() {
        return txid;
    }

    public void setTxid(String txid) {
        this.txid = txid;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
    }

    public String getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(String scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
