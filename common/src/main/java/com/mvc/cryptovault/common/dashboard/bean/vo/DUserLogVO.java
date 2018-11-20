package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 15:48
 */
@Data
public class DUserLogVO implements Serializable {
    private static final long serialVersionUID = 6652709376730471341L;

    @ApiModelProperty("用户uid")
    private BigInteger id;
    @ApiModelProperty("创建时间")
    private Long createdAt;
    @ApiModelProperty("消息内容")
    private String message;
}
