package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 16:55
 */
@Data
public class DProjectVO implements Serializable {
    private static final long serialVersionUID = -4966954208005363409L;

    @ApiModelProperty("项目id")
    private BigInteger id;

    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("基础令牌id")
    private BigInteger baseTokenId;

    @ApiModelProperty("基础令牌名称")
    private String baseTokenName;

    @ApiModelProperty("项目状态0即将开始 1进行中 2已结束 9取消")
    private Integer status;

    @ApiModelProperty("开始时间")
    private Long startedAt;

    @ApiModelProperty("结束时间")
    private Long stopAt;

    @ApiModelProperty("众筹总量")
    private BigDecimal projectTotal;

    @ApiModelProperty("兑换比例")
    private Float ratio;

    @ApiModelProperty("项目币种")
    private String tokenName;

    @ApiModelProperty("令牌id")
    private BigInteger tokenId;

}
