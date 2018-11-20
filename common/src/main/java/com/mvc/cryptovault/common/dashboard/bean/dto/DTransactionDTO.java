package com.mvc.cryptovault.common.dashboard.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 15:58
 */
@Data
public class DTransactionDTO implements Serializable {
    private static final long serialVersionUID = -2563993469891012397L;

    @ApiModelProperty("交易对id")
    private BigInteger pairId;

    @ApiModelProperty("交易类型 1购买 2出售")
    private Integer transactionType;

    @ApiModelProperty("订单号")
    private String orderNumber;

    @ApiModelProperty("手机号")
    private String cellphone;

    @ApiModelProperty("订单状态 0未完成 1全部完成 4取消")
    private Integer status;
}
