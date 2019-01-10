package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/1/10 16:48
 */
@Data
@ApiModel("忘记密码 ")
public class PasswordDTO {
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("临时密码")
    private String token;

}
