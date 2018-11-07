package com.mvc.cryptovault.app.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
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
    private BigDecimal password;
}
