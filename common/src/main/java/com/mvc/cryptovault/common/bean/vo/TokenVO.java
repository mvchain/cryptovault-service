package com.mvc.cryptovault.common.bean.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qiyichen
 * @create 2018/11/12 20:09
 */
@Data
public class TokenVO implements Serializable {

    private static final long serialVersionUID = 4150483687690621965L;

    private String token;
    private String refreshToken;

}
