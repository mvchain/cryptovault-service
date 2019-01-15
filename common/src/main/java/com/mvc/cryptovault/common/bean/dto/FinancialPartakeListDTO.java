package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/12 14:34
 */
@Data
@ApiModel("收益检索条件")
public class FinancialPartakeListDTO {

    @ApiModelProperty("记录id,为0或为空时从头拉取")
    private BigInteger id;
    @ApiModelProperty("分页大小,默认10条")
    private Integer pageSize = 10;

}
