package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/12 14:44
 */
@Data
@ApiModel("区块链浏览器交易详情")
public class ExplorerTransactionDetailVO {

    @ApiModelProperty("交易hash")
    private String hash;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("确认数")
    private Integer confirm;
    @ApiModelProperty("区块高度")
    private BigInteger height;
    @ApiModelProperty("数量")
    private BigDecimal value;
    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("转出地址")
    private String from;
    @ApiModelProperty("转入地址")
    private String to;

}
