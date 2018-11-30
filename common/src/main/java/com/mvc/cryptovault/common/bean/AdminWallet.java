package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * admin_user
 */
@Table(name = "admin_wallet")
@Data
public class AdminWallet implements Serializable {
    /**
     *
     */
    @Id
    @Column(name = "id")
    private BigInteger id;

    /**
     * 是否为热钱包
     */
    @Column(name = "is_hot")
    private Integer isHot;

    /**
     * 用户昵称
     */
    @Column(name = "address")
    private String address;

    /**
     *
     */
    @Column(name = "balance")
    private BigDecimal balance;

    /**
     * 1ETH 2BTC
     */
    @Column(name = "block_type")
    private Integer blockType;

    @Column(name = "pv_key")
    private String pvKey;

}