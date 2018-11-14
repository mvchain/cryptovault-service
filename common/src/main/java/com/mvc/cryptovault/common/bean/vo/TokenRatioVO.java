package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 15:58
 */
@Data
@ApiModel("资产比值")
public class TokenRatioVO {

    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("对usdt比值 ")
    private BigDecimal ratio;

}
