package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2018/11/7 18:56
 */
@Data
@ApiModel("挂单所需数据")
public class OrderInfoVO {

    @ApiModelProperty("基础货币余额")
    private BigDecimal balance;

    @ApiModelProperty("目标货币余额")
    private BigDecimal tokenBalance;

    @ApiModelProperty("当前价格")
    private BigDecimal price;

    @ApiModelProperty("浮动最小值,单位百分比")
    private Float min;

    @ApiModelProperty("浮动最大值,单位百分比")
    private Float max;

    @ApiModelProperty("剩余可购买数量(仅对指定挂单进行购买时,展示为该笔订单还剩多少未成交额)")
    private BigDecimal value;

    @ApiModelProperty("最小挂单数量")
    private BigDecimal minLimit;
}
