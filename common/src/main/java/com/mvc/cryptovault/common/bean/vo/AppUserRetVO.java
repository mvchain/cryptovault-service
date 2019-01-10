package com.mvc.cryptovault.common.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @author qiyichen
 * @create 2019/1/10 15:21
 */
@Data
@ApiModel("助记词相关信息")
public class AppUserRetVO {

    private String privateKey;
    private List<String> mnemonics;

}
