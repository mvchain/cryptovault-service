package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/13 15:47
 */
@Data
public class TimeSearchDTO {

    private BigInteger timestamp;
    @ApiModelProperty("0上拉 1下拉")
    private Integer type;
}
