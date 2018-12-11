package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/10 14:19
 */
@Data
public class TokenVolume {

    @Id
    private BigInteger id;
    private BigInteger tokenId;
    private BigDecimal value;
    private Long createdAt;
    private Integer used;
}
