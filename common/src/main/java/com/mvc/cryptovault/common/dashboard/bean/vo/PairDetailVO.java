package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiyichen
 * @create 2018/11/20 17:57
 */
@Data
public class PairDetailVO implements Serializable {

    private static final long serialVersionUID = -888009820562646531L;
    @ApiModelProperty("是否展示")
    private Integer visible;

    @ApiModelProperty("是否交易")
    private Integer transaction;

}
