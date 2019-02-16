package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2019/2/16 14:02
 */
@Data
@ApiModel("发币数据详情")
public class ProjectPublishDetailVO extends ProjectSimpleVO {

    @ApiModelProperty("成功预约数量")
    private BigDecimal successValue;
    @ApiModelProperty("预约数量")
    private BigDecimal value;
    @ApiModelProperty("成功支付金额")
    private BigDecimal successPayed;
    @ApiModelProperty("支付金额")
    private BigDecimal payed;

}
