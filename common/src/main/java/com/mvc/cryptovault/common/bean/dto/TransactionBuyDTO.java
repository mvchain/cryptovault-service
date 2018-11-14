package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 18:26
 */
@Data
@ApiModel("挂单交易输入参数")
public class TransactionBuyDTO {

    @ApiModelProperty("数量")
    private BigDecimal value;

    @ApiModelProperty("当前交易对")
    private BigInteger pairId;

    @ApiModelProperty("挂单价格")
    private BigDecimal price;

    @ApiModelProperty("订单id")
    private BigInteger id;

    @ApiModelProperty("密码")
    private String password;
}
