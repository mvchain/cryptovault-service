package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/2/13 19:55
 */
@Data
public class SignSumVO {

    @ApiModelProperty("币种名称")
    private String tokenName;
    @ApiModelProperty("币种id")
    private BigInteger tokenId;
    @ApiModelProperty("提现金额")
    private BigDecimal total;
    @ApiModelProperty("提现交易笔数")
    private Integer num;

}
