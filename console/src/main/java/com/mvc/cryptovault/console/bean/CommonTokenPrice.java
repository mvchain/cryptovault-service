package com.mvc.cryptovault.console.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * common_token_price
 */
@Table(name = "common_token_price")
@Data
public class CommonTokenPrice implements Serializable {
    /**
     * 令牌id
     */
    @Id
    @Column(name = "token_id")
    private BigInteger tokenId;

    /**
     * 令牌价格（对usdt）
     */
    @Column(name = "token_price")
    private BigDecimal tokenPrice;

    /**
     * common_token_price
     */
    private static final long serialVersionUID = 1L;

    /**
     * 令牌id
     * @return token_id 令牌id
     */
    public BigInteger getTokenId() {
        return tokenId;
    }

    /**
     * 令牌id
     * @param tokenId 令牌id
     */
    public void setTokenId(BigInteger tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * 令牌价格（对usdt）
     * @return token_price 令牌价格（对usdt）
     */
    public BigDecimal getTokenPrice() {
        return tokenPrice;
    }

    /**
     * 令牌价格（对usdt）
     * @param tokenPrice 令牌价格（对usdt）
     */
    public void setTokenPrice(BigDecimal tokenPrice) {
        this.tokenPrice = tokenPrice;
    }
}