package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;


/**
 * @author qiyichen
 * @create 2018/11/20 12:03
 */
@Data
public class AdminVO implements Serializable {
    private static final long serialVersionUID = 2208988110320592383L;
    @ApiModelProperty("管理员id")
    private BigInteger id;
    @ApiModelProperty("账户名称")
    private String username;
    @ApiModelProperty("账户昵称")
    private String nickname;
    @ApiModelProperty("账户状态1可用 0禁用")
    private Integer status;
    @ApiModelProperty("管理员类型 0主管理员 1子管理员")
    private Integer adminType;
}
