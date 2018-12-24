package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 15:12
 */
@Data
@ApiModel("转账信息简略信息")
public class TransactionSimpleVO {

    @ApiModelProperty("1转入 2转出")
    private Integer transactionType;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("转账创建时间戳")
    private Long createdAt;
    @ApiModelProperty("转账更新时间戳")
    private Long updatedAt;
    @ApiModelProperty("转账金额")
    private BigDecimal value;
    @ApiModelProperty("当前比率，单位为USDT，计算资产时需要使用")
    private BigDecimal ratio;
    @ApiModelProperty("转账状态[0待打包 1确认中 2打包成功 9打包失败]")
    private Integer status;
    @ApiModelProperty("交易分类[0区块链交易 1订单交易 2众筹交易（包含众筹和由众筹引起的释放）3划账]")
    private Integer classify;
    @ApiModelProperty("id")
    private BigInteger id;
    @ApiModelProperty("备注字段,如众筹项目类型则为项目名称,交易则为交易对名称")
    private String orderRemark;
}
