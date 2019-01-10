package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qiyichen
 * @create 2019/1/10 16:04
 */
@Data
@ApiModel("助记词校验")
public class MnemonicsDTO {

    @ApiModelProperty("邮箱地址")
    @NotNull
    private String email;

    @ApiModelProperty("组合后的助记词字符串,用逗号分隔")
    @NotNull
    private String mnemonics;
}
