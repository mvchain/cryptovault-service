package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/13 15:52
 */
@Data
@ApiModel("浏览器订单详情")
public class ExplorerOrder {

    @ApiModelProperty("转账来源")
    private String from;
    @ApiModelProperty("转账目标")
    private String to;
    @ApiModelProperty("类型0转账 1交易")
    private Integer classify;
    @ApiModelProperty("买方令牌id")
    private BigInteger buyTokenId;
    @ApiModelProperty("买方令牌名称")
    private String buyTokenName;
    @ApiModelProperty("卖方令牌id")
    private BigInteger sellTokenId;
    @ApiModelProperty("卖方令牌名称")
    private String sellTokenName;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("买方令牌数量")
    private BigDecimal buyValue;
    @ApiModelProperty("卖方令牌数量")
    private BigDecimal sellValue;

}
