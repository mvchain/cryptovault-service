package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2018/11/7 18:09
 */
@Data
@ApiModel("交易对查询")
public class PairDTO {

    @ApiModelProperty("1 VRT交易 2余额交易,不传为所有")
    private Integer pairType;
}
