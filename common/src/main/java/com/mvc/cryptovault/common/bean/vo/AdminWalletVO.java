package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiyichen
 * @create 2018/12/8 13:59
 */
@Data
public class AdminWalletVO implements Serializable {
    private static final long serialVersionUID = 2282037268059375770L;

    @ApiModelProperty("比特系冷钱包中心账户地址")
    private String usdtCold;
    @ApiModelProperty("以太系冷钱包地址")
    private String ethCold;
    @ApiModelProperty("以太系热钱包地址")
    private String ethHot;
    @ApiModelProperty("比特系热钱包中心账户")
    private String usdtHot;
    @ApiModelProperty("比特系可用地址库存")
    private Integer usdtAddressCount;
    @ApiModelProperty("以太系可用地址库存")
    private Integer ethAddressCount;

}
