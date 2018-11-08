package com.mvc.cryptovault.app.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2018/11/8 15:06
 */
@Data
public class PageDTO {

    @ApiModelProperty("分页大小,默认10条")
    private Integer pageSize = 10;
}
