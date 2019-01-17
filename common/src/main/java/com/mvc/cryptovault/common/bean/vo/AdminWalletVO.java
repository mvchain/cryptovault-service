package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2018/12/8 13:59
 */
@Data
public class AdminWalletVO implements Serializable {
    private static final long serialVersionUID = 2282037268059375770L;

    @ApiModelProperty("待汇总金额")
    private BigDecimal waitBalance;
    @ApiModelProperty("地址库存")
    private Integer count;
    @ApiModelProperty("冷钱包地址余额")
    private BigDecimal coldBalance;
    @ApiModelProperty("冷钱包地址")
    private String coldAddress;
    @ApiModelProperty("热钱包地址余额")
    private BigDecimal hotBalance;
    @ApiModelProperty("热钱包地址")
    private String hotAddress;
    @ApiModelProperty("币种名称")
    private String tokenName;

}
