package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 15:52
 */
@Data
public class DTransactionVO implements Serializable {
    private static final long serialVersionUID = 8921086233008805528L;

    @ApiModelProperty("创建时间")
    private Long createdAt;

    @ApiModelProperty("更新时间")
    private Long updatedAt;

    @ApiModelProperty("交易对名")
    private String pairName;

    @ApiModelProperty("挂单数量")
    private BigDecimal value;

    @ApiModelProperty("成交数量")
    private BigDecimal deal;

    @ApiModelProperty("待成交数量")
    private BigDecimal surplus;

    @ApiModelProperty("挂单价格")
    private BigDecimal price;

    @ApiModelProperty("交易类型 1购买 2出售")
    private Integer transactionType;

    @ApiModelProperty("订单状态 0未完成 1全部完成 4取消")
    private Integer status;

    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("手机号")
    private String cellphone;

    @ApiModelProperty
    private BigInteger id;

    public String getTransactionTypeStr() {
        return transactionType == 1 ? "购买" : "出售";
    }

    public String getStatusStr() {
        if (status == 0) {
            return "挂单中";
        } else if (status == 1) {
            return "交易完成";
        } else {
            return "已撤销";
        }
    }
}
