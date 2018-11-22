package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * common_pair
 */
@Table(name = "common_pair")
@Data
public class CommonPair implements Serializable {
    /**
     * 交易对id
     */
    @Id

    @Column(name = "id")
    private BigInteger id;

    /**
     * 交易对名称
     */
    @Column(name = "pair_name")
    private String pairName;

    /**
     * 基础货币令牌id
     */
    @Column(name = "base_token_id")
    private BigInteger baseTokenId;

    /**
     * 兑换货币id
     */
    @Column(name = "token_id")
    private BigInteger tokenId;

    /**
     * 基础货币令牌名称
     */
    @Column(name = "base_token_name")
    private String baseTokenName;

    private Integer status;

    /**
     * 兑换货币名称
     */
    @Column(name = "token_name")
    private String tokenName;

    /**
     * 手续费
     */
    @Column(name = "fee")
    private Float fee;

    /**
     * common_pair
     */
    private static final long serialVersionUID = 1L;

    /**
     * 交易对id
     *
     * @return id 交易对id
     */
    public BigInteger getId() {
        return id;
    }

    /**
     * 交易对id
     *
     * @param id 交易对id
     */
    public void setId(BigInteger id) {
        this.id = id;
    }

    /**
     * 交易对名称
     *
     * @return pair_name 交易对名称
     */
    public String getPairName() {
        return pairName;
    }

    /**
     * 交易对名称
     *
     * @param pairName 交易对名称
     */
    public void setPairName(String pairName) {
        this.pairName = pairName;
    }

    /**
     * 基础货币令牌id
     *
     * @return base_token_id 基础货币令牌id
     */
    public BigInteger getBaseTokenId() {
        return baseTokenId;
    }

    /**
     * 基础货币令牌id
     *
     * @param baseTokenId 基础货币令牌id
     */
    public void setBaseTokenId(BigInteger baseTokenId) {
        this.baseTokenId = baseTokenId;
    }

    /**
     * 兑换货币id
     *
     * @return token_id 兑换货币id
     */
    public BigInteger getTokenId() {
        return tokenId;
    }

    /**
     * 兑换货币id
     *
     * @param tokenId 兑换货币id
     */
    public void setTokenId(BigInteger tokenId) {
        this.tokenId = tokenId;
    }

    /**
     * 手续费
     *
     * @return fee 手续费
     */
    public Float getFee() {
        return fee;
    }

    /**
     * 手续费
     *
     * @param fee 手续费
     */
    public void setFee(Float fee) {
        this.fee = fee;
    }
}