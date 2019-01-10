package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/1/10 20:15
 */
@Data
@ApiModel("修改密码")
public class AppUserEmailDTO {

    @ApiModelProperty("验证码")
    private String valiCode;
}
