package com.mvc.cryptovault.app.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qiyichen
 * @create 2018/11/7 14:01
 */
@Data
@ApiModel("预约列表条件")
public class ReservationDTO {

    @NotNull(message = "{RESERVATION_TYPE_NULL}")
    @ApiModelProperty("0已预约 1成功的")
    private Integer reservationType;
}
