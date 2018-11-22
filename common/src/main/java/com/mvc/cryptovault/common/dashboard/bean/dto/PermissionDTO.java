package com.mvc.cryptovault.common.dashboard.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/21 13:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO implements Serializable {

    @ApiModelProperty("权限id")
    private BigInteger permissionId;

    @ApiModelProperty("权限开关")
    private Integer status;

}
