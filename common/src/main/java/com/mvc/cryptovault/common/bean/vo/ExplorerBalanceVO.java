package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/13 15:46
 */
@Data
@ApiModel("区块链余额信息")
public class ExplorerBalanceVO {

    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("余额")
    private BigDecimal value;

}
