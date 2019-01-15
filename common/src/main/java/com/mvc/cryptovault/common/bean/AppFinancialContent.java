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
public class AppFinancialContent {

    @Id
    private BigInteger financialId;
    private String content;
    private String rule;

}
