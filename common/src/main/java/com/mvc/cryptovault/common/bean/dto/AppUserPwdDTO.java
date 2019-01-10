package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/1/10 19:13
 */
@Data
@ApiModel("用户密码修改")
public class AppUserPwdDTO {
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("新密码")
    private String newPassword;
}
