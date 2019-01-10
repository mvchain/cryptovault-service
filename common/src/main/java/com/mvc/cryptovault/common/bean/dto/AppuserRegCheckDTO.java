package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qiyichen
 * @create 2019/1/10 14:37
 */
@ApiModel("注册信息校验")
@Data
public class AppuserRegCheckDTO {

    @ApiModelProperty("注册邀请码")
    @NotNull
    private String inviteCode;
    @ApiModelProperty("邮箱地址")
    @NotNull
    private String email;
    @NotNull
    @ApiModelProperty("验证码")
    private String valiCode;

}
