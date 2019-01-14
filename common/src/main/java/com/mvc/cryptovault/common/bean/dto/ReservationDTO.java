package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 14:01
 */
@Data
@ApiModel("预约列表条件")
public class ReservationDTO {

    @ApiModelProperty("上一条记录id,如果为0或不存在则重头拉取,否则从目标位置记录增量拉取")
    private BigInteger id;

    @ApiModelProperty("0上拉 1下拉")
    private Integer type;

    @ApiModelProperty("项目名称")
    private String projectName;

    private Integer pageSize = 10;
}
