package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * common_token_control_next
 */
@Table(name = "common_token_control_next")
@Data
public class CommonTokenControlNext implements Serializable {
    /**
     * 
     */
    @Id
    @Column(name = "token_id")
    private BigInteger tokenId;

    /**
     * 预计下个涨跌目标值
     */
    @Column(name = "next_price")
    private BigDecimal nextPrice;

    /**
     * 1涨 2跌
     */
    @Column(name = "next_type")
    private Integer nextType;

    /**
     * 当前进度总量
     */
    private BigDecimal totalSuccess;

    /**
     * 下一个小浮动价格
     */
    private BigDecimal floatPrice;

    /**
     * common_token_control_next
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return token_id 
     */
    public BigInteger getTokenId() {
        return tokenId;
    }

    /**
     * 
     * @param tokenId 
     */
    public void setTokenId(BigInteger tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * 预计下个涨跌目标值
     * @return next_price 预计下个涨跌目标值
     */
    public BigDecimal getNextPrice() {
        return nextPrice;
    }

    /**
     * 预计下个涨跌目标值
     * @param nextPrice 预计下个涨跌目标值
     */
    public void setNextPrice(BigDecimal nextPrice) {
        this.nextPrice = nextPrice;
    }

    /**
     * 1涨 2跌
     * @return next_type 1涨 2跌
     */
    public Integer getNextType() {
        return nextType;
    }

    /**
     * 1涨 2跌
     * @param nextType 1涨 2跌
     */
    public void setNextType(Integer nextType) {
        this.nextType = nextType;
    }
}