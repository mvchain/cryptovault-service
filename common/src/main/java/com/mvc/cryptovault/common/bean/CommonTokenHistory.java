package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/14 15:23
 */
@Table(name = "common_token_history")
@Data
public class CommonTokenHistory implements Serializable {
    private static final long serialVersionUID = -6330252943851673476L;

    @Id

    @Column(name = "id")
    private BigInteger id;

    @Column(name = "token_id")
    private BigInteger tokenId;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "price")
    private BigDecimal price;
}
