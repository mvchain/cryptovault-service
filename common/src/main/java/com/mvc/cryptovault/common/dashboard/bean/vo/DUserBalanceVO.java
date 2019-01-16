package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 15:39
 */
@Data
public class DUserBalanceVO implements Serializable {
    private static final long serialVersionUID = -6123277389824702232L;

    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("数量 ")
    private BigDecimal value;
    @ApiModelProperty("估算价值")
    private BigDecimal balance;
    @ApiModelProperty("地址")
    private String address;
}
