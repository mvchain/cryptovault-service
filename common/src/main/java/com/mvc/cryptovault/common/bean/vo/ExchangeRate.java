package com.mvc.cryptovault.common.bean.vo;

import lombok.Data;

@Data
public class ExchangeRate {

    private Float bankConversionPri;
    private String date;
    private String time;
}