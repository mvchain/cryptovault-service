package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 15:38
 */
@Data
public class DUSerDetailVO implements Serializable {
    private static final long serialVersionUID = 4808718229905903738L;

    @ApiModelProperty("用户id")
    private BigInteger id;
    @ApiModelProperty("用户昵称")
    private String nickname;
}
