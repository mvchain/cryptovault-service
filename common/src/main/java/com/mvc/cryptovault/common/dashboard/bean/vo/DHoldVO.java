package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 17:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DHoldVO implements Serializable {

    private static final long serialVersionUID = 6136328477724712972L;
    @ApiModelProperty("币种名称")
    private String tokenName;

    @ApiModelProperty("币种id")
    private BigInteger tokenId;

    @ApiModelProperty("金额")
    private BigDecimal value;
}
