package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 14:36
 */
@Data
@ApiOperation("转账输入类")
public class TransactionDTO {

    @ApiModelProperty("地址")
    @NotNull(message = "{ADDRESS_NULL}")
    private String address;

    @ApiModelProperty("金额")
    @NotNull(message = "{VALUE_NULL}")
    private BigDecimal value;

    @ApiModelProperty("交易密码")
    @NotNull(message = "{PASSWORD_NULL}")
    private String password;

    @ApiModelProperty("币种id")
    private BigInteger tokenId;
}
