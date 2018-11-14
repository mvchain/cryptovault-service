package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 16:06
 */
@Data
@ApiModel("币种详情")
public class TokenDetailVO {
    @ApiModelProperty("币种名称")
    private String tokenName;
    @ApiModelProperty("令牌图片")
    private String tokenImage;
    @ApiModelProperty("令牌中文名")
    private String tokenCnName;
    @ApiModelProperty("令牌英文名")
    private String tokenEnName;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
}
