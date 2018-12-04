package com.mvc.cryptovault.common.bean;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.Generated;

/**
 * app_user_address
 */
@Table(name = "app_user_address")
@Data
public class AppUserAddress implements Serializable {
    /**
     * 
     */
    @Id
    @Column(name = "user_id")
    private BigInteger userId;

    /**
     * 令牌id
     */
    @Column(name = "token_id")
    private BigInteger tokenId;

    /**
     * 收款地址
     */
    @Column(name = "address")
    private String address;

    /**
     * app_user_address
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return user_id 
     */
    public BigInteger getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId 
     */
    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

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
     * 收款地址
     * @return address 收款地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 收款地址
     * @param address 收款地址
     */
    public void setAddress(String address) {
        this.address = address;
    }
}