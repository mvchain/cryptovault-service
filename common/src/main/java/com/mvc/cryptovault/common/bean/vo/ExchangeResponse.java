package com.mvc.cryptovault.common.bean.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExchangeResponse {

    private Integer resultcode;
    private String reason;
    private Map<String, JSONObject> result;
}