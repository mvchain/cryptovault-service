package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/8 16:30
 */
@Data
@ApiModel("项目简略信息")
public class ProjectSimpleVO {

    @ApiModelProperty("项目id")
    private BigInteger projectId;
    @ApiModelProperty("项目状态0即将开始 1进行中 2已结束")
    private Integer status;
    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("限购")
    private BigDecimal projectLimit;
    @ApiModelProperty("基础货币缩写")
    private String baseTokenName;
    @ApiModelProperty("基础货币id")
    private BigInteger baseTokenId;
    @ApiModelProperty("币种名称")
    private String tokenName;
    @ApiModelProperty("货币id")
    private BigInteger tokenId;
    @ApiModelProperty("项目创建时间")
    private Long createdAt;
    @ApiModelProperty("项目更新时间")
    private Long updatedAt;
    @ApiModelProperty("释放比例")
    private Float releaseValue;
    @ApiModelProperty("众筹规模")
    private BigDecimal total;
    @ApiModelProperty("基础货币兑换比例")
    private Float ratio;
    @ApiModelProperty("开始时间")
    private Long startedAt;
    @ApiModelProperty("结束时间")
    private Long stopAt;
    @ApiModelProperty("项目图标")
    private String projectImage;

}
