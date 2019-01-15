package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/12 14:31
 */
@Data
@ApiModel("收益信息")
public class FinancialPartakeListVO {

    @ApiModelProperty("记录id")
    private BigInteger id;
    @ApiModelProperty("收益")
    private BigDecimal income;
    @ApiModelProperty("理财剩余签到次数")
    private Long createdAt;
    @ApiModelProperty("收益货币名称")
    private String tokenName;

}
