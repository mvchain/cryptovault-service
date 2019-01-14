package com.mvc.cryptovault.common.dashboard.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qiyichen
 * @create 2018/11/22 17:41
 */
@Data
public class DUserDTO {

    @ApiModelProperty("用户登录账户")
    private String username;

    @ApiModelProperty("用户密码")
    @NotNull(message = "{PASSWORD_EMPTY}")
    private String password;

    @ApiModelProperty("验证码.登录时返回的错误次数过多时需要输入")
    private String validCode;
}
