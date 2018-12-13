package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/8 16:44
 */
@Data
@ApiModel("参与的项目")
public class PurchaseVO {
    @ApiModelProperty("项目id")
    private BigInteger projectId;
    @ApiModelProperty("参与项目记录id")
    private BigInteger id;
    @ApiModelProperty("项目名称")
    private String projectName;
    @ApiModelProperty("基础货币兑换比例")
    private Float ratio;
    @ApiModelProperty("释放比例")
    private Float releaseValue;
    @ApiModelProperty("购买数量")
    private BigDecimal value;
    @ApiModelProperty("支付金额")
    private BigDecimal price;
    @ApiModelProperty("购买时间")
    private Long createdAt;
    @ApiModelProperty("发币时间")
    private Long publishAt;
    @ApiModelProperty("结束时间")
    private Long stopAt;
    @ApiModelProperty("0等待结算 1成功的 9众筹失败")
    private Integer reservationType;
    @ApiModelProperty("众筹订单号")
    private String projectOrderId;
    @ApiModelProperty("基础货币名称")
    private String baseTokenName;
    @ApiModelProperty("众筹货币名称")
    private String tokenName;
    @ApiModelProperty("成功购买金额")
    private BigDecimal successPayed;
    @ApiModelProperty("成功购买数量")
    private BigDecimal successValue;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;

}
