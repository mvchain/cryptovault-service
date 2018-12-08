package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/7 15:19
 */
@Table(name = "app_project_partake")
@Data
public class AppProjectPartake implements Serializable {

    @Id
    private BigInteger id;
    private BigInteger projectId;
    private BigInteger userId;
    private BigDecimal value;
    private Integer times;
    private BigDecimal reverseValue;
    private Long publishTime;
    private BigInteger tokenId;

}
