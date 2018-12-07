package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/7 13:51
 */
@Data
public class ImportPartake implements Serializable {
    private static final long serialVersionUID = -1222042980780731424L;

    private BigInteger userId;
    @ApiModelProperty("一个文件只处理一个项目")
    private BigInteger projectId;
    @ApiModelProperty("允许的数量,不足的部分将被退换金额")
    private BigDecimal value;

}
