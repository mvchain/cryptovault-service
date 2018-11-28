package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 18:36
 */
@Data
@ApiModel("交易对简易信息")
public class DPairVO {

    @ApiModelProperty("交易对名称")
    private String pairName;

    @ApiModelProperty("交易对ID")
    private BigInteger pairId;
}
