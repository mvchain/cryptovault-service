package com.mvc.cryptovault.common.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/9 16:13
 */
@Data
public class AppFinancialDetail {

    @Id
    @ApiModelProperty("理财项目id,不需要设置")
    private BigInteger financialId;
    @ApiModelProperty("层级")
    private Integer depth;
    @ApiModelProperty("利率")
    private Float ratio;

}
