package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/9 16:18
 */
@Data
public class AppUserFinancialPartake {
    @Id
    private BigInteger id;
    private BigInteger userId;
    private BigInteger financialId;
    private Long createdAt;
    private Long updatedAt;
    private BigDecimal value;
    private Integer times;
    private BigDecimal shadowValue;
    private Integer status;
    private BigDecimal income;
    private String orderNumber;
    private BigInteger tokenId;
    private BigInteger baseTokenId;

}
