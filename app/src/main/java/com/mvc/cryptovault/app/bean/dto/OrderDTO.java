package com.mvc.cryptovault.app.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/8 15:01
 */
@Data
@ApiModel("获取交易订单")
public class OrderDTO extends PageDTO {

    @ApiModelProperty("交易对信息，如USDT/VRT")
    private String pair;
    @ApiModelProperty("交易类型 1购买 2出售")
    private Integer transactionType;
    @ApiModelProperty("当前订单id，不传为从第一条获取，否则以传入订单的id为初始记录继续增量查询")
    private BigInteger orderId;

}
