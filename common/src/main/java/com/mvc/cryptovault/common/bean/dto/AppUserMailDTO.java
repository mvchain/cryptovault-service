package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/1/10 19:15
 */
@Data
@ApiModel("郵箱修改")
public class AppUserMailDTO {

    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("验证码")
    private String valiCode;
    @ApiModelProperty("临时令牌")
    private String token;

}
