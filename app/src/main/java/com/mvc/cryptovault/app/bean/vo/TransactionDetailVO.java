package com.mvc.cryptovault.app.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2018/11/7 15:22
 */
@Data
@ApiModel("转账详情")
public class TransactionDetailVO {

    @ApiModelProperty("交易创建时间")
    private Long createdAt;

    @ApiModelProperty("订单更新日期")
    private Long updatedAt;

    @ApiModelProperty("订单状态0待打包 1区块确认中 2交易陈宫 9交易失败")
    private Integer status;

    @ApiModelProperty("确认数，完成状态的判断依据")
    private Integer depth;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("手续费币种名称")
    private String feeTokenType;

    @ApiModelProperty("交易hash对应网页")
    private String hashLink;

    @ApiModelProperty("交易额")
    private BigDecimal value;

    @ApiModelProperty("币种名称")
    private String tokenType;

    @ApiModelProperty("交易hash")
    private String hash;

    @ApiModelProperty("目标地址")
    private String toAddress;

    @ApiModelProperty("转出地址，可能为多个")
    private String[] fromAddress;

}