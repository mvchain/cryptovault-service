package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2019/1/12 14:00
 */
@Data
@ApiModel("理财持仓详情")
public class FinancialPartakeDetailVO {

    @ApiModelProperty("理财产品名称")
    private String financialName;
    @ApiModelProperty("最小年化收益")
    private Float incomeMin;
    @ApiModelProperty("最大年化收益")
    private Float incomeMax;
    @ApiModelProperty("取现天数")
    private Integer times;
    @ApiModelProperty("投资金额")
    private BigDecimal value;
    @ApiModelProperty("累计收益")
    private BigDecimal income;
    @ApiModelProperty("收益货币名称")
    private String tokenName;
    @ApiModelProperty("基础货币名称")
    private String baseTokenName;

}
