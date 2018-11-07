package com.mvc.cryptovault.app.bean.vo;

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
    @ApiModelProperty("转账金额")
    private BigDecimal value;
    @ApiModelProperty("当前比率，单位为USDT，计算资产时需要使用")
    private BigDecimal ratio;

}
