package com.mvc.cryptovault.common.bean.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2019/2/20 15:14
 */
@Data
@ApiModel("验证信息输入")
public class ValidDTO {

    private String geetest_challenge;
    private String geetest_validate;
    private String geetest_seccode;
    private String uid;
    private Integer status;

}
