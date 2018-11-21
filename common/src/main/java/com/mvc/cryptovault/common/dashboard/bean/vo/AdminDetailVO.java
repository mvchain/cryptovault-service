package com.mvc.cryptovault.common.dashboard.bean.vo;

import com.mvc.cryptovault.common.dashboard.bean.dto.PermissionDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/20 15:19
 */
@Data
public class AdminDetailVO implements Serializable {
    private static final long serialVersionUID = 3311554223912214875L;

    @ApiModelProperty("管理员昵称")
    private String nickname;

    @ApiModelProperty("用户账户")
    private String username;

    @ApiModelProperty("权限列表")
    private List<PermissionDTO> permissionList;

    @ApiModelProperty("权限列表(逗号分隔)")
    private String permissions;

    @ApiModelProperty("启用开关")
    private Integer status;

}

