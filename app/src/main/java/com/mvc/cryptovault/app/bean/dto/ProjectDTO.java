package com.mvc.cryptovault.app.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author qiyichen
 * @create 2018/11/7 13:55
 */
@Data
@ApiModel("项目搜索")
public class ProjectDTO {

    @ApiModelProperty("0即将预约 1预约中 2已结束")
    @NotNull(message = "{PROJECT_TYPE_NULL}")
    private Integer projectType;
}
