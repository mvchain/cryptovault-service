package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/8 14:27
 */
@Data
@ApiModel("消息")
public class MessageVO {

    @ApiModelProperty("消息id")
    private BigInteger id;

    @ApiModelProperty("转账结果（0失败 1成功）")
    private Integer status;

    @ApiModelProperty("消息内容")
    private String message;

    @ApiModelProperty("消息内容分类，如转账信息或购买信息，和内容id配合用于后续的点击跳转到指定页面")
    private String messageType;

    @ApiModelProperty("消息内容id，用于页面跳转")
    private BigInteger messageId;

    @ApiModelProperty("消息创建时间")
    private Long createdAt;

    @ApiModelProperty("消息更新时间（暂时只有已读操作）")
    private Long updatedAt;

    @ApiModelProperty("0未读 1已读")
    private Integer read;
}
