package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2018/12/4 15:05
 */
@Data
@ApiModel("用户钱包展示状态操作")
public class AssertVisibleDTO {

    @ApiModelProperty("新添加的币种tokenId列表,用英文逗号隔开,可以有空格,不能以逗号结尾,错误的数据将会被忽略")
    private String addTokenIdArr;

    @ApiModelProperty("新移除的币种tokenId列表,用英文逗号隔开,可以有空格,不能以逗号结尾,错误的数据将会被忽略")
    private String removeTokenIdArr;

}
