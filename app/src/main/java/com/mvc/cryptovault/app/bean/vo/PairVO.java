package com.mvc.cryptovault.app.bean.vo;

import com.alibaba.fastjson.JSON;
import com.mvc.cryptovault.common.bean.vo.Result;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiyichen
 * @create 2018/11/7 18:36
 */
@Data
@ApiModel("交易对信息")
public class PairVO {

    @ApiModelProperty("涨跌幅,正数涨,负数跌.单位为%")
    private Float increase;

    @ApiModelProperty("交易对信息")
    private String pair;

    @ApiModelProperty("对usdt价格")
    private BigDecimal ratio;

    @ApiModelProperty("令牌名称")
    private String tokenName;

    @ApiModelProperty("图标地址，必须缓存")
    private String tokenImage;


}
