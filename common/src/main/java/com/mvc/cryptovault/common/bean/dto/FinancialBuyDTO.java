package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2019/1/12 14:37
 */
@Data
@ApiModel("理财购买信息")
public class FinancialBuyDTO {

    @ApiModelProperty("购买数量")
    private BigDecimal value;
    @ApiModelProperty("交易密码")
    private String transactionPassword;

}
