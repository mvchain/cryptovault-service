package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/12 20:09
 */
@Data
public class TokenVO implements Serializable {

    private static final long serialVersionUID = 4150483687690621965L;

    @ApiModelProperty("用户唯一标识id,可用于推送时指定为alias")
    private BigInteger userId;
    private String token;
    private String refreshToken;

}
