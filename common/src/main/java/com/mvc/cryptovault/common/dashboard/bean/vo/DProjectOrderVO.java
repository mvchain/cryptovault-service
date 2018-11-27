package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 17:23
 */
@Data
public class DProjectOrderVO implements Serializable {

    private static final long serialVersionUID = -2959375044042658750L;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("订单号")
    private String orderNumber;
    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("项目id")
    private String projectId;
    @ApiModelProperty("用户手机号")
    private String cellphone;
    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("购买数量")
    private BigDecimal value;
    @ApiModelProperty("支付金额")
    private BigDecimal payed;
    @ApiModelProperty("基础货币名称")
    private String baseTokenName;
    @ApiModelProperty("基础货币id")
    private BigInteger baseTokenId;
    @ApiModelProperty("订单状态 0等待 1成功 4取消 9失败")
    private Integer status;
    @ApiModelProperty("项目状态 0即将开始 1进行中 2已结束 3发币中 9取消")
    private Integer projectStatus;
    @ApiModelProperty("id")
    private BigInteger id;


}
