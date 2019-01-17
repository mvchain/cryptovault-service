package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author qiyichen
 * @create 2019/1/17 13:53
 */
@Data
@ApiModel("最新价格")
public class TickerVO {

    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal price;

}
