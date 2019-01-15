package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/12 17:44
 */
@Data
@ApiModel("个人持仓信息")
public class FinancialUserPartakeVO {

    @ApiModelProperty("理财项目名称")
    private String name;
    @ApiModelProperty("收益")
    private BigDecimal value;
    @ApiModelProperty("持仓金额")
    private BigDecimal partake;
    @ApiModelProperty("货币名称")
    private String tokenName;
    @ApiModelProperty("基础货币名称")
    private String baseTokenName;
    @ApiModelProperty("剩余提取次数")
    private Integer times;
    @ApiModelProperty("记录id")
    private BigInteger id;
}
