package com.mvc.cryptovault.common.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/9 16:16
 */
@Data
public class AppFinancial {

    private BigInteger id;
    private String name;
    private BigInteger baseTokenId;
    private BigInteger tokenId;
    private String baseTokenName;
    private String tokenName;
    private Float ratio;
    private Integer depth;
    private Float incomeMin;
    private Float incomeMax;
    private BigInteger startAt;
    private BigInteger stopAt;
    private BigDecimal limit;
    private BigDecimal userLimit;
    private BigInteger times;

}
