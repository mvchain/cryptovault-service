package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 16:04
 */
@Data
public class OverTransactionVO implements Serializable {
    private static final long serialVersionUID = -7869572159975149575L;

    @ApiModelProperty("创建时间")
    private Long createdAt;

    @ApiModelProperty("更新时间(最终成交时间)")
    private Long updatedAt;

    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("父订单号")
    private String parentOrderNumber;

    @ApiModelProperty("交易对")
    private String pairName;

    @ApiModelProperty("交易类型 1购买 2出售")
    private Integer transactionType;

    @ApiModelProperty("手机号")
    private String cellphone;

    @ApiModelProperty("交易数量")
    private BigDecimal value;

    @ApiModelProperty("交易价格")
    private BigDecimal price;

    @ApiModelProperty("基础交易币种名称(币种单位)")
    private String baseTokenName;

    @ApiModelProperty("id")
    private BigInteger id;

    @ApiModelProperty("父订单id")
    private BigInteger parentId;

    public String getTransactionTypeStr() {
        return transactionType == 1 ? "购买" : "出售";
    }
}
