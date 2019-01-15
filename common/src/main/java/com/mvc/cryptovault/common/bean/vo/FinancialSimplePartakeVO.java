package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/11 18:45
 */
@Data
@ApiModel("理财产品详情")
public class FinancialSimplePartakeVO {

    @ApiModelProperty("产品id")
    private BigInteger id;
    @ApiModelProperty("产品名称")
    private String name;
    @ApiModelProperty("参与币种id")
    private BigInteger baseTokenId;
    @ApiModelProperty("奖励币种id")
    private BigInteger tokenId;
    @ApiModelProperty("奖励币种名称")
    private String baseTokenName;
    @ApiModelProperty("奖励币种名称")
    private String tokenName;
    @ApiModelProperty("兑换比例")
    private Float ratio;
    @ApiModelProperty("最小浮动年化")
    private Float incomeMin;
    @ApiModelProperty("最大浮动年化")
    private Float incomeMax;
    @ApiModelProperty("开始日期")
    private Long startAt;
    @ApiModelProperty("结束日期")
    private Long stopAt;
    @ApiModelProperty("理财总量")
    private BigDecimal limitValue;
    @ApiModelProperty("用户限购")
    private BigDecimal userLimit;
    @ApiModelProperty("理财天数")
    private Integer times;
    @ApiModelProperty("最小购买量")
    private BigDecimal minValue;

}
