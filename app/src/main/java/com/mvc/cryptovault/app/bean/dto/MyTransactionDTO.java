package com.mvc.cryptovault.app.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2018/11/7 18:19
 */
@Data
@Api("我参与的交易筛选参数")
public class MyTransactionDTO {

    @ApiModelProperty("交易对,不区分大小写")
    private String pair;

    @ApiModelProperty("订单状态0进行中 1完成")
    private Integer status;

    @ApiModelProperty("订单类型 0买单 1卖单")
    private Integer transactionType;

}
