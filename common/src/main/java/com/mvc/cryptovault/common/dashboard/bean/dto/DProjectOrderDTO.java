package com.mvc.cryptovault.common.dashboard.bean.dto;

import com.mvc.cryptovault.common.bean.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiyichen
 * @create 2018/11/20 17:29
 */
@Data
public class DProjectOrderDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = 2112236548179184L;
    @ApiModelProperty("订单状态 0未完成 1全部完成 4取消")
    private Integer status;

    @ApiModelProperty("手机号")
    private String cellphone;

    @ApiModelProperty("项目名称")
    private String projectName;
}
