package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 17:06
 */
@Data
public class DProjectDetailVO implements Serializable {

    private static final long serialVersionUID = -8488749225596544517L;
    @ApiModelProperty("id")
    private BigInteger id;

    @ApiModelProperty("项目图标")
    private String projectName;

    @ApiModelProperty("基础货币id")
    private BigInteger baseTokenId;

    @ApiModelProperty("货币id")
    private BigInteger tokenId;

    @ApiModelProperty("基础货币名称")
    private String baseTokenName;

    @ApiModelProperty("项目图片")
    private String projectImage;

    @ApiModelProperty("是否展示")
    private Integer visiable;

    @ApiModelProperty("开始急死俺")
    private Long startedAt;

    @ApiModelProperty("结束时间")
    private Long stopAt;

    @ApiModelProperty("众筹总量")
    private BigDecimal projectTotal;

    @ApiModelProperty("兑换比例")
    private Float ratio;

    @ApiModelProperty("释放比例")
    private Float releaseValue;

    @ApiModelProperty("限购数量")
    private BigDecimal projectLimit;

    @ApiModelProperty("最小购买数量")
    private BigDecimal projectMin;

    @ApiModelProperty("发布时间")
    private Long publishAt;
}
