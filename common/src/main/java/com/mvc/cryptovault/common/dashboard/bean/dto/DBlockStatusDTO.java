package com.mvc.cryptovault.common.dashboard.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiyichen
 * @create 2018/11/20 17:47
 */
@Data
public class DBlockStatusDTO implements Serializable {

    @ApiModelProperty("id列表,用英文逗号隔开")
    private String ids;

    @ApiModelProperty("同意 2拒绝")
    private Integer status;
}
