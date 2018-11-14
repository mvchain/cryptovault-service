package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

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

    @ApiModelProperty("上一条记录id,如果为0或不存在则重头拉取,否则从目标位置记录增量拉取")
    private BigInteger projectId;

    @ApiModelProperty("0上拉 1下拉")
    private Integer type;
    private Integer pageSize = 10;
}
