package com.mvc.cryptovault.common.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * @author qiyichen
 * @create 2018/10/9 17:22
 */
@Data
public class OrderEntity {

    private String sign;
    private String jsonStr;
    private JSONObject ext;

}
