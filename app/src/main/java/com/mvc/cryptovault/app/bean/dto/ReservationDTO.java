package com.mvc.cryptovault.app.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 14:01
 */
@Data
@ApiModel("预约列表条件")
public class ReservationDTO extends PageDTO {

    @NotNull(message = "{RESERVATION_TYPE_NULL}")
    @ApiModelProperty("0已预约 1成功的")
    private Integer reservationType;

    @ApiModelProperty("上一条记录id,如果为0或不存在则重头拉取,否则从目标位置记录增量拉取")
    private BigInteger id;

}
