package com.mvc.cryptovault.app.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 18:48
 */
@Data
@ApiModel("订单简略信息")
public class OrderVO {

    @ApiModelProperty("用户头像地址")
    private String headImage;
    @ApiModelProperty("购买限额(当前可购买剩余数量)")
    private BigDecimal limitValue;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("所挂订单总量")
    private BigDecimal total;
    @ApiModelProperty("订单id")
    private BigInteger orderId;
}
