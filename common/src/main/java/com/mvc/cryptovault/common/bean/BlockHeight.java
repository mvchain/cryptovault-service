package com.mvc.cryptovault.common.bean;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * block_height
 */
@Table(name = "block_height")
@Data
public class BlockHeight implements Serializable {
    /**
     * 令牌id
     */
    @Id
    @Column(name = "token_id")
    private BigInteger tokenId;

    /**
     * 当前高度
     */
    @Column(name = "height")
    private Integer height;

    /**
     * block_height
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
     * 当前高度
     * @return height 当前高度
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * 当前高度
     * @param height 当前高度
     */
    public void setHeight(Integer height) {
        this.height = height;
    }
}