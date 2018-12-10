package com.mvc.cryptovault.common.dashboard.bean.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/20 18:11
 */
@Data
public class DTokenTransSettingVO implements Serializable {

    private static final long serialVersionUID = -990267242688841884L;
    @ApiModelProperty("id")
    private BigInteger tokenId;

    @ApiModelProperty("涨停百分比下限")
    private Float increaseMin;

    @ApiModelProperty("涨停百分比上限")
    private Float increaseMax;

    @ApiModelProperty("跌停百分比下限")
    private Float decreaseMin;

    @ApiModelProperty("跌停百分比上限")
    private Float decreaseMax;

    @ApiModelProperty("购买下限")
    private Float buyMin;

    @ApiModelProperty("购买上限")
    private Float buyMax;

    @ApiModelProperty("出售下限")
    private Float sellMin;

    @ApiModelProperty("出售上限")
    private Float sellMax;

    @ApiModelProperty("价格博定交易金额量")
    private BigDecimal priceBase;

    @ApiModelProperty("下个基准价格")
    private BigDecimal nextPrice;

    @ApiModelProperty("波动下限")
    private Float waveMin;

    @ApiModelProperty("波动上限")
    private Float waveMax;

    @ApiModelProperty("开盘价格")
    private BigDecimal startPrice;

    @ApiModelProperty("开始过状态")
    private Integer startStatus;

    @ApiModelProperty("交易状态")
    private Integer transactionStatus;

    @ApiModelProperty("最小购买量")
    private BigDecimal minLimit;
}
