package com.mvc.cryptovault.common.bean.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/11/13 11:47
 */
@Data
public class PageDTO implements Serializable {
    private static final long serialVersionUID = 4191235024358352864L;

    private Integer pageNum;
    private Integer pageSize = 10;
    private Long updatedStopAt;
    private Long updatedStartAt;
    private Long createdStopAt;
    private Long createdStartAt;
    private String orderBy;
}
