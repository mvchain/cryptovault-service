package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/8 15:20
 */
@Data
@ApiModel("我参与的订单")
public class MyOrderVO {

    @ApiModelProperty("订单id")
    private BigInteger id;
    @ApiModelProperty("订单号（前台显示的订单号）")
    private String orderNumber;
    @ApiModelProperty("交易对")
    private BigInteger pairId;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("交易类型 1购买 2出售")
    private Integer transactionType;
    @ApiModelProperty("成交价格")
    private BigDecimal price;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("更新时间")
    private Long updatedAt;
    @ApiModelProperty("成交数量")
    private BigDecimal deal;
    @ApiModelProperty("订单状态0进行中 1完成")
    private Integer status;

}
