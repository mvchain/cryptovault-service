package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/9 16:19
 */
@Data
public class AppUserFinancialIncome {
    @Id
    private BigInteger id;
    private BigInteger userId;
    private BigInteger financialId;
    private Long createdAt;
    private Long updatedAt;
    private BigInteger tokenId;
    private String tokenName;
    private BigDecimal value;
    private BigInteger partakeId;

}
