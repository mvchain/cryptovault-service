package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/11 18:39
 */
@Data
@ApiModel("理财简略信息")
public class FinancialSimpleVO {

    @ApiModelProperty("产品名称")
    private String name;
    @ApiModelProperty("最小年化收益")
    private Float incomeMin;
    @ApiModelProperty("最大年化收益")
    private Float incomeMax;
    @ApiModelProperty("理财项目id")
    private BigInteger id;
    @ApiModelProperty("最小购买数量")
    private BigDecimal minValue;
    @ApiModelProperty("结束时间戳")
    private Long stopAt;
    @ApiModelProperty("产品期限（天）")
    private Integer times;
    @ApiModelProperty("基础货币名称")
    private String baseTokenName;
    @ApiModelProperty("产品总额度")
    private BigDecimal limitValue;
    @ApiModelProperty("产品已购买额度")
    private BigDecimal sold;
    @ApiModelProperty("是否需要签到")
     private Integer needSign;

}