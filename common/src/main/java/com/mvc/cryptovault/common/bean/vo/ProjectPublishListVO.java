package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/2/16 14:08
 */
@Data
@ApiModel("发币记录列表信息")
public class ProjectPublishListVO {

    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("数量")
    private BigDecimal value;
    @ApiModelProperty("币种id")
    private BigInteger tokenId;
    @ApiModelProperty("币种名称")
    private String tokenName;
    @ApiModelProperty("记录id")
    private BigInteger id;
}
