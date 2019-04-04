package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author qiyichen
 * @create 2019/1/10 14:23
 */
@Data
@ApiModel("app用户注册")
public class AppUserDTO implements Serializable {

//    @ApiModelProperty("注册邀请码")
//    @NotNull
//    private String inviteCode;
    @ApiModelProperty("邮箱地址")
    @NotNull
    private String email;
    @ApiModelProperty("昵称")
    @NotNull
    private String nickname;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("交易密码")
    private String transactionPassword;
    @ApiModelProperty("token")
    private String token;
    @ApiModelProperty("salt")
    private String salt;

}
