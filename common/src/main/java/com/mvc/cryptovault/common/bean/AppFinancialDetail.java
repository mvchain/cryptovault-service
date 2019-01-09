package com.mvc.cryptovault.common.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/9 16:13
 */
@Data
public class AppFinancialDetail {

    private BigInteger financialId;
    private Integer depth;
    private Float ratio;

}
