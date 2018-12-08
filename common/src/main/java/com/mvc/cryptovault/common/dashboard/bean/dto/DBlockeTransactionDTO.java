package com.mvc.cryptovault.common.dashboard.bean.dto;

import com.mvc.cryptovault.common.bean.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiyichen
 * @create 2018/11/20 17:39
 */
@Data
public class DBlockeTransactionDTO extends PageDTO implements Serializable {

    private static final long serialVersionUID = -5826639195867550038L;

    @ApiModelProperty("订单号")
    private String orderNumber;
    @ApiModelProperty("目标地址")
    private String toAddress;
    @ApiModelProperty("来源地址")
    private String fromAddress;
    @ApiModelProperty("1. 待审核2. 待签名（审核通过后3. 拒绝4. 正在提币（导入签名文件后5. 提币成功（交易确认成功后6. 失败")
    private Integer transactionStatus;
    @ApiModelProperty("操作类型 1充值 2提现")
    private Integer oprType;
    @ApiModelProperty("是否为管理员操作,默认为非管理员")
    private Integer isAdmin;


}
