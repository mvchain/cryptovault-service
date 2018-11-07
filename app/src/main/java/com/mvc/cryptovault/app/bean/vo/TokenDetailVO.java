package com.mvc.cryptovault.app.bean.vo;

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
    @ApiModelProperty("令牌全称")
    private String tokenFullname;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
}
