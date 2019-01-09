package com.mvc.cryptovault.common;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/9 16:18
 */
@Data
public class AppUserFinancialPartake {

    private BigInteger userId;
    private BigInteger financialId;
    private BigInteger createdAt;
    private BigInteger updatedAt;
    private BigDecimal value;

}
