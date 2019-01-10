package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/7 15:52
 */
@Data
@ApiModel("用户简略信息")
public class UserSimpleVO {

    @ApiModelProperty("用户昵称")
    private String nickname;
    @ApiModelProperty("用户登录账户，这里为手机号，中间4位隐藏")
    private String username;

}
