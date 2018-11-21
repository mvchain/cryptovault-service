package com.mvc.cryptovault.common.dashboard.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/20 15:23
 */
@Data
public class AdminDTO implements Serializable {
    private static final long serialVersionUID = -7457474668128349210L;

    @ApiModelProperty("id,新建试传0或者空")
    private BigInteger id;

    @ApiModelProperty("账号名称")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("密码,新建可传,修改时传入无效,需要使用的单独的密码修改接口")
    private String password;

    @ApiModelProperty("账号状态")
    private Integer status;

    @ApiModelProperty("权限列表")
    private List<PermissionDTO> permissionList;

}
