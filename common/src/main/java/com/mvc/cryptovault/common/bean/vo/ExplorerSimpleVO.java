package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/12 14:34
 */
@Data
@ApiModel("区块简略信息")
public class ExplorerSimpleVO {

    @ApiModelProperty("区块高度")
    private BigInteger blockId;
    @ApiModelProperty("交易笔数")
    private Integer transactions;
    @ApiModelProperty("生产时间")
    private Long createdAt;

}
