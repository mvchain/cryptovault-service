package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2018/11/7 15:46
 */
@Data
@ApiModel("转账信息获取")
public class TransactionTokenVO {

    @ApiModelProperty("手续费对应令牌名称")
    private String feeTokenName;
    @ApiModelProperty("余额")
    private BigDecimal balance;
    @ApiModelProperty("手续费")
    private Float fee;
}
