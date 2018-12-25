package com.mvc.cryptovault.common.dashboard.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 16:28
 */
@Data
public class DTokenDTO implements Serializable {
    private static final long serialVersionUID = -3752941585357319021L;

    @ApiModelProperty("令牌图片地址")
    private String tokenImage;

    @ApiModelProperty("令牌id")
    private BigInteger tokenId;

    @ApiModelProperty("令牌名称")
    private String tokenName;

    @ApiModelProperty("令牌英文名称")
    private String tokenEnName;

    @ApiModelProperty("令牌中文名称")
    private String tokenCnName;

    @ApiModelProperty("小数位数")
    private Integer decimals;

    @ApiModelProperty("合约地址")
    private String contractAddress;

    @ApiModelProperty("ETH或空.为空时代表非真实代币")
    private String blockType;

}
