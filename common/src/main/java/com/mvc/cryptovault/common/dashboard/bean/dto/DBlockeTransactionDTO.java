package com.mvc.cryptovault.common.dashboard.bean.dto;

import com.mvc.cryptovault.common.bean.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiyichen
 * @create 2018/11/20 17:39
 */
@Data
public class DBlockeTransactionDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = -5826639195867550038L;

    @ApiModelProperty("订单号")
    private String orderNumber;
    @ApiModelProperty("目标地址")
    private String toAddress;
    @ApiModelProperty("0打包中 1确认中 2确认完毕 9失败")
    private Integer status;
}
