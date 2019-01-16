package com.mvc.cryptovault.common.bean.dto;

import com.mvc.cryptovault.common.bean.AppFinancial;
import com.mvc.cryptovault.common.bean.AppFinancialContent;
import com.mvc.cryptovault.common.bean.AppFinancialDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2019/1/15 17:24
 */
@Data
@ApiModel("理财产品编辑详情")
public class AppFinancialDTO {

    @ApiModelProperty("理财项目id")
    private BigInteger id;
    @ApiModelProperty("理财名称")
    private String name;
    @ApiModelProperty("基础货币id")
    private BigInteger baseTokenId;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("基础货币名称")
    private String baseTokenName;
    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("兑换比例")
    private Float ratio;
    @ApiModelProperty("有效层级")
    private Integer depth;
    @ApiModelProperty("最小年化")
    private Float incomeMin;
    @ApiModelProperty("最大年化")
    private Float incomeMax;
    @ApiModelProperty("开始时间")
    private Long startAt;
    @ApiModelProperty("结束时间")
    private Long stopAt;
    @ApiModelProperty("项目总量")
    private BigDecimal limitValue;
    @ApiModelProperty("单个用户限购")
    private BigDecimal userLimit;
    @ApiModelProperty("所需签到天数")
    private Integer times;
    @ApiModelProperty("最小起购数量")
    private BigDecimal minValue;
    @ApiModelProperty("是否展示")
    private Integer visible;
    @ApiModelProperty("产品规则")
    private AppFinancialContent content;
    @ApiModelProperty("产品提成详情")
    private List<AppFinancialDetail> details;

}
