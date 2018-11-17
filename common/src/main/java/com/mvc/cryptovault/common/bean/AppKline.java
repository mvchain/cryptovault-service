package com.mvc.cryptovault.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.Generated;

/**
 * app_kline
 */
@Table(name = "app_kline")
@Data
public class AppKline implements Serializable {
    /**
     * 
     */
    @Id

    @Column(name = "id")
    private BigInteger id;

    /**
     * k线时间轴
     */
    @Column(name = "kline_time")
    private Long klineTime;

    /**
     * 价格
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 交易对id
     */
    @Column(name = "pair_id")
    private BigInteger pairId;

    /**
     * app_kline
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return id 
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 
     * @param id 
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     * 价格
     * @return price 价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 价格
     * @param price 价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 交易对id
     * @return pair_id 交易对id
     */
    public BigInteger getPairId() {
        return pairId;
    }

    /**
     * 交易对id
     * @param pairId 交易对id
     */
    public void setPairId(BigInteger pairId) {
        this.pairId = pairId;
    }
}