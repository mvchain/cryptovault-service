package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2019/1/11 18:52
 */
@Data
@ApiModel("理财统计")
public class FinancialBalanceVO {

    @ApiModelProperty("余额")
    private BigDecimal balance;
    @ApiModelProperty("收益")
    private BigDecimal income;
    @ApiModelProperty("今日收益")
    private BigDecimal lastIncome;

}
