package com.mvc.cryptovault.common.dashboard.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 15:34
 */
@Data
public class DUSerVO implements Serializable {
    private static final long serialVersionUID = 472113159034856827L;

    @ApiModelProperty("手机号")
    private String cellphone;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("估算余额,单位为usdt,需要在对应页面获取当前价格比值并按条件切换")
    private BigDecimal balance;
    @ApiModelProperty("用户id")
    private BigInteger id;
    @ApiModelProperty("用户状态0禁用 1启用")
    private Integer status;

}
