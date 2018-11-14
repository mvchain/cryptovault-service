package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2018/11/7 14:30
 */
@Data
@ApiModel("划账参数")
public class DebitDTO {

    @ApiModelProperty("划账金额")
    @NotNull(message = "{DEBIT_NULL}")
    private BigDecimal value;

    @ApiModelProperty("交易密码")
    @NotNull(message = "{PASSWORD_NULL}")
    private String password;
}
