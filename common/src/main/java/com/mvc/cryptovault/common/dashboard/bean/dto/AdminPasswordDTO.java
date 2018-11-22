package com.mvc.cryptovault.common.dashboard.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 15:30
 */
@Data
public class AdminPasswordDTO  implements Serializable {
    private static final long serialVersionUID = 290292301216150732L;


    @ApiModelProperty("当前密码")
    private String password;

    @ApiModelProperty("新密码")
    private String newPassword;

    @ApiModelProperty
    private BigInteger userId;
}
