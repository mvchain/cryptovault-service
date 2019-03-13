package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/13 15:49
 */
@Data
@ApiModel("区块链订单简略信息")
public class ExplorerSimpleOrder {

    @ApiModelProperty("类型0转账 1交易")
    private Integer classify;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("记录id")
    private BigInteger id;
}
