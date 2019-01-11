package com.mvc.cryptovault.common.bean.dto;

import com.github.pagehelper.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/10 20:46
 */
@Data
@ApiModel("推薦人查詢")
public class RecommendDTO {

    @ApiModelProperty("分页大小,默认10条")
    private Integer pageSize = 10;
    @ApiModelProperty("最后一条记录id, 为空或为0时从头拉取")
    private BigInteger inviteUserId;
    @ApiModelProperty("用户id，不需要传,后台自行设置")
    private BigInteger userId;
}
