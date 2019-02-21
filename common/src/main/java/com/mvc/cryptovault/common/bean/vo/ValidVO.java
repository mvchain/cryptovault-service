package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/2/20 15:11
 */
@Data
@ApiModel("图片验证信息获取")
public class ValidVO {

    @ApiModelProperty("uid")
    private String uid;
    @ApiModelProperty("验证所需信息")
    private String result;
    @ApiModelProperty("初始化状态")
    private Integer status;
}
