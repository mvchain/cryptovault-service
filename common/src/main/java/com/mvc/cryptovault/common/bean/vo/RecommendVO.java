package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/10 20:24
 */
@Data
@ApiModel("推荐人列表,使用最后一条数据的id作为分页条件")
public class RecommendVO {

    @ApiModelProperty("用户id")
    private BigInteger userId;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("用户创建时间")
    private Long createdAt;
}
