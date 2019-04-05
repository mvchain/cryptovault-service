package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/9 16:16
 */
@Data
public class AppFinancial {
    @Id
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
    private Long startAt;
    private Long stopAt;
    private Long createdAt;
    private Long updatedAt;
    private BigDecimal userLimit;
    private Integer times;
    private BigDecimal minValue;
    private Integer status;
    private BigDecimal limitValue;
    private BigDecimal sold;
    private Integer visible;
    private BigDecimal addSold;
    private Float showIncomeMin;
    private Float showIncomeMax;

}
