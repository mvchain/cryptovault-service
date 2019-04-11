package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/16 10:51
 */
@Data
@ApiModel("理财项目")
public class AppFinancialVO {

    @ApiModelProperty("理财项目id")
    private BigInteger id;
    @ApiModelProperty("理财名称")
    private String name;
    @ApiModelProperty("基础货币id")
    private BigInteger baseTokenId;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("基础货币名称")
    private String baseTokenName;
    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("兑换比例")
    private Float ratio;
    @ApiModelProperty("有效层级")
    private Integer depth;
    @ApiModelProperty("最小年化")
    private Float incomeMin;
    @ApiModelProperty("最大年化")
    private Float incomeMax;
    @ApiModelProperty("开始时间")
    private Long startAt;
    @ApiModelProperty("结束时间")
    private Long stopAt;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("更新时间")
    private Long updatedAt;
    @ApiModelProperty("项目总量")
    private BigDecimal limitValue;
    @ApiModelProperty("单个用户限购")
    private BigDecimal userLimit;
    @ApiModelProperty("所需签到天数")
    private Integer times;
    @ApiModelProperty("最小起购数量")
    private BigDecimal minValue;
    @ApiModelProperty("状态0默认 1开始 2结束")
    private Integer status;
    @ApiModelProperty("是否展示")
    private Integer visible;
    @ApiModelProperty("次日提成百分比")
    private Float nextIncome;
    @ApiModelProperty("展示收益最小百分比")
    private Float showIncomeMin;
    @ApiModelProperty("展示收益最大百分比")
    private Float showIncomeMax;
    @ApiModelProperty("附加卖出百分比")
    private BigDecimal addSold;
    @ApiModelProperty("是否需要签到")
    private Integer needSign;

}
