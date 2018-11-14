package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2018/11/8 15:09
 */
@Data
@ApiModel("K线数据")
public class KLineVO {

    @ApiModelProperty("时间坐标X数组")
    private Long[] timeX;
    @ApiModelProperty("价格坐标Y数组")
    private BigDecimal[] valueY;

}
