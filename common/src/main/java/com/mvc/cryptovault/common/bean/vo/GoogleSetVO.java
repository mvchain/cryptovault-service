package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/4/3 17:51
 */
@Data
public class GoogleSetVO {

    @ApiModelProperty("登录密码")
    private String password;
    @ApiModelProperty("谷歌验证码")
    private Integer googleCode;
    @ApiModelProperty("开关设置,0关闭 1开启")
    private Integer status;
    @ApiModelProperty("google secret")
    private String googleSecret;

}
