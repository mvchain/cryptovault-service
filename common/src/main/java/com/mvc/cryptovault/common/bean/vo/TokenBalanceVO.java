package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 15:00
 */
@Data
@ApiModel("余额信息")
public class TokenBalanceVO {

    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("余额,资产价值需要根据当前资产对自行计算（余额 * 对USDT比率 * USDT价值）")
    private BigDecimal value;
    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty( "当前比率，单位为CNY，计算资产时需要使用")
    private BigDecimal ratio;
    @ApiModelProperty( "币种图片，冗余返回")
    private String tokenImage;
}
