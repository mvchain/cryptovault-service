package com.mvc.cryptovault.common.bean.vo;

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

    @ApiModelProperty("订单状态0待打包 1区块确认中 2交易成功 9交易失败")
    private Integer status;

    @ApiModelProperty("手续费")
    private BigDecimal fee;

    @ApiModelProperty("手续费币种名称")
    private String feeTokenType;

    @ApiModelProperty("交易hash对应网页")
    private String hashLink;

    @ApiModelProperty("交易额")
    private BigDecimal value;

    @ApiModelProperty("币种名称")
    private String tokenName;

    @ApiModelProperty("交易hash")
    private String blockHash;

    @ApiModelProperty("目标地址")
    private String toAddress;

    @ApiModelProperty("转出地址")
    private String fromAddress;

    @ApiModelProperty("交易分类[0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）]3划账")
    private Integer classify;

    @ApiModelProperty("备注字段,如众筹项目类型则为项目名称,交易则为交易对名称")
    private String orderRemark;

    @ApiModelProperty("1转入 2转出")
    private Integer transactionType;
}
