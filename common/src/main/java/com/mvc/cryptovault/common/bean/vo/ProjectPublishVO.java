package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/2/16 13:55
 */
@Data
@ApiModel("已发币项目简略信息")
public class ProjectPublishVO {

    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("发币时间")
    private Long publishTime;
    @ApiModelProperty("释放比例")
    private Float releaseValue;
    @ApiModelProperty("项目id")
    private BigInteger projectId;
    @ApiModelProperty("项目图片")
    private String projectImage;

}