package com.mvc.cryptovault.console.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * app_order_detail
 */
@Table(name = "app_order_detail")
@Data
public class AppOrderDetail implements Serializable {
    /**
     * 
     */
    @Id
    @Column(name = "order_id")
    private BigInteger orderId;

    /**
     * 
     */
    @Column(name = "created_at")
    private BigInteger createdAt;

    /**
     * 
     */
    @Column(name = "updated_at")
    private BigInteger updatedAt;

    /**
     * 交易金额变动
     */
    @Column(name = "value")
    private BigDecimal value;

    /**
     * 交易手续费
     */
    @Column(name = "fee")
    private BigDecimal fee;

    /**
     * 交易hash
     */
    @Column(name = "hash")
    private String hash;

    /**
     * 
     */
    @Column(name = "to_address")
    private String toAddress;

    /**
     * 
     */
    @Column(name = "from_address")
    private String fromAddress;

    /**
     * app_order_detail
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return order_id 
     */
    public BigInteger getOrderId() {
        return orderId;
    }

    /**
     * 
     * @param orderId 
     */
    public void setOrderId(BigInteger orderId) {
        this.orderId = orderId;
    }

    /**
     * 
     * @return created_at 
     */
    public BigInteger getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt 
     */
    public void setCreatedAt(BigInteger createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return updated_at 
     */
    public BigInteger getUpdatedAt() {
        return updatedAt;
    }

    /**
     * 
     * @param updatedAt 
     */
    public void setUpdatedAt(BigInteger updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * 交易金额变动
     * @return value 交易金额变动
     */
    public BigDecimal getValue() {
        return value;
    }

    /**
     * 交易金额变动
     * @param value 交易金额变动
     */
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    /**
     * 交易手续费
     * @return fee 交易手续费
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 交易手续费
     * @param fee 交易手续费
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    /**
     * 交易hash
     * @return hash 交易hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * 交易hash
     * @param hash 交易hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }

    /**
     * 
     * @return to_address 
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * 
     * @param toAddress 
     */
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    /**
     * 
     * @return from_address 
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * 
     * @param fromAddress 
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }
}