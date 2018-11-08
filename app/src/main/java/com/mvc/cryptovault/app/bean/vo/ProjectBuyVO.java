package com.mvc.cryptovault.app.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2018/11/8 16:54
 */
@Data
@ApiModel("项目购买基础信息")
public class ProjectBuyVO {

    @ApiModelProperty("可用基础货币余额")
    private BigDecimal balance;
    @ApiModelProperty("限购额， 为项目限购额-当前已够额")
    private BigDecimal limitValue;
}
