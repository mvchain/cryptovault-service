package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/12 14:40
 */
@Data
@ApiModel("浏览器交易简略信息")
public class ExplorerTransactionSimpleVO {

    @ApiModelProperty("交易hash")
    private String hash;
    @ApiModelProperty("区块高度")
    private BigInteger height;
    @ApiModelProperty("确认数")
    private BigInteger confirm;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("交易id,用于分页")
    private BigInteger transactionId;

}
