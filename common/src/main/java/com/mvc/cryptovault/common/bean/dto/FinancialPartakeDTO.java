package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/11 18:48
 */
@Data
@ApiModel("持仓搜索条件")
public class FinancialPartakeDTO {

    @ApiModelProperty("最后一条记录ID,为空或0时从头拉取")
    private BigInteger id;
    @ApiModelProperty("持仓类型 1计息中 2待提取 3已提取")
    private Integer financialType;
    @ApiModelProperty("分页大小,默认10条")
    private Integer pageSize = 10;
}
