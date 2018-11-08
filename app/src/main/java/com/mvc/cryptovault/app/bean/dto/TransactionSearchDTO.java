package com.mvc.cryptovault.app.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2018/11/7 14:17
 */
@Data
@ApiModel("资产转账列表查询条件")
public class TransactionSearchDTO {

    @ApiModelProperty("1转入 2转出 不传则为全部")
    private Integer transactionType;
}
