package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 14:17
 */
@Data
@ApiModel("资产转账列表查询条件")
public class TransactionSearchDTO {

    @ApiModelProperty("1转入 2转出 传0则为全部")
    private Integer transactionType;

    @ApiModelProperty("订单种类[0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）3划账 4理財]")
    private Integer classify;

    @ApiModelProperty("上一条记录id,如果为0或不存在则重头拉取,否则从目标位置记录增量拉取")
    private BigInteger id;

    private Integer pageSize = 10;

    @ApiModelProperty("0上拉 1下拉")
    private Integer type;

    @ApiModelProperty("令牌id")
    private BigInteger tokenId;

}

