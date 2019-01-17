package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/15 17:29
 */
@Data
@ApiModel("理财订单列表")
public class AppFinancialOrderVO {

    @ApiModelProperty("记录id")
    private BigInteger id;
    @ApiModelProperty("订单号")
    private String orderNumber;
    @ApiModelProperty("用户昵称")
    private String nickname;
    @ApiModelProperty("邮箱地址")
    private String email;
    @ApiModelProperty("里拆产品名称")
    private String financialName;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("购买金额")
    private BigDecimal value;
    @ApiModelProperty("收益")
    private BigDecimal income;
    @ApiModelProperty("基础货币id")
    private BigInteger baseTokenId;
    @ApiModelProperty("基础货币名称")
    private String baseTokenName;
    @ApiModelProperty("币种id")
    private BigInteger tokenId;
    @ApiModelProperty("币种名称")
    private String tokenName;
    @ApiModelProperty("价值（cny）")
    private BigDecimal price;
    @ApiModelProperty("状态 1计息中 2待提取 3已提取")
    private Integer status;

    public String getStatusStr() {
        String str = "";
        switch (status) {
            case 1:
                str = "计息中";
                break;
            case 2:
                str = "待提取";
                break;
            case 3:
                str = "已提取";
                break;
        }
        return str;
    }

    public String getValueStr() {
        return value.stripTrailingZeros().toPlainString() + " " + baseTokenName;
    }

    public String getIncomeStr() {
        return income.stripTrailingZeros().toPlainString()+ " " + tokenName;
    }

    public String getPriceStr() {
        return "￥" + price.stripTrailingZeros().toPlainString();
    }
}
