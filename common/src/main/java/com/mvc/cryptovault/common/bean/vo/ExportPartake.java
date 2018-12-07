package com.mvc.cryptovault.common.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/7 13:48
 */
@Data
public class ExportPartake implements Serializable {
    private static final long serialVersionUID = 1375594815206966129L;

    private BigInteger projectId;
    private String cellphone;
    private String nickname;
    private String projectName;
    private BigInteger userId;
    private BigDecimal value;
    private String tokenName;
    private String baseTokenName;
}
