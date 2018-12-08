package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/8 14:20
 */
@Data
public class AdminTransactionDTO {

    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("提币数量")
    private BigDecimal value;
    @ApiModelProperty("提币地址")
    private String toAddress;
    @ApiModelProperty("主管理员密码")
    private String password;

}
