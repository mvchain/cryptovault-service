package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author qiyichen
 * @create 2018/12/3 10:34
 */
@Data
public class ExchangeRateVO implements Serializable {
    private static final long serialVersionUID = -3196288843269302276L;

    @ApiModelProperty("法币缩写")
    private String name;
    @ApiModelProperty("法币汇率,以人名币计价,保留6位小数")
    private Float value;
}
