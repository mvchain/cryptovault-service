package com.mvc.cryptovault.common.bean;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("理财项目id,不需要设置")
    private BigInteger financialId;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("规则")
    private String rule;

}
