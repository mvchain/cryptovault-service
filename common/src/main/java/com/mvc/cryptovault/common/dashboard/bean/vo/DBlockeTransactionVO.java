package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 17:40
 */
@Data
public class DBlockeTransactionVO implements Serializable {
    private static final long serialVersionUID = 288414515722154818L;

    @ApiModelProperty("id")
    private BigInteger id;
    @ApiModelProperty("交易hash")
    private String hash;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("修改时间")
    private Long updatedAt;
    @ApiModelProperty("令牌id")
    private BigInteger tokenId;
    @ApiModelProperty("令牌名称")
    private String tokenName;
    @ApiModelProperty("1充值 2提现")
    private Integer oprType;
    @ApiModelProperty("用户id")
    private BigInteger userId;
    @ApiModelProperty("0打包中 1确认中 2确认完毕 9失败")
    private Integer status;
    @ApiModelProperty("错误描述")
    private String errorMsg;
    @ApiModelProperty("错误详情")
    private String errorData;
    @ApiModelProperty("交易数量")
    private BigDecimal value;
    @ApiModelProperty("来源地址")
    private String fromAddress;
    @ApiModelProperty("目标地址")
    private String toAddress;
    @ApiModelProperty("订单id")
    private String orderNumber;
    @ApiModelProperty("手机")
    private String cellphone;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("修改订单状态(1. 待审核2. 待签名（审核通过后3. 拒绝4. 正在提币（导入签名文件后5. 提币成功（交易确认成功后6. 失败（交易确认失败后）)")
    private Integer transactionStatus;

    public String getTransactionStatusStr() {
        switch (transactionStatus) {
            case 1:
                return "待审核";
            case 2:
                return "待签名";
            case 3:
                return "拒绝";
            case 4:
                return "正在提币";
            case 5:
                return "提币成功";
            case 6:
                return "失败";
            default:
                return "失败";
        }
    }

}
