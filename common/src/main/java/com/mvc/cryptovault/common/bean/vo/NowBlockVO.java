package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/12 14:30
 */
@Data
@ApiModel("当前区块信息")
public class NowBlockVO {

    @ApiModelProperty("区块高度")
    private BigInteger blockId;
    @ApiModelProperty("总交易数量")
    private BigInteger transactionCount;
    @ApiModelProperty("确认时间")
    private Long confirmTime;
    @ApiModelProperty("当前版本")
    private String version;
    @ApiModelProperty("发行总量")
    private BigInteger total;
    @ApiModelProperty("当前服务器时间")
    private Long serviceTime;

}
