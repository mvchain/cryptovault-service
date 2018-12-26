package com.mvc.cryptovault.common.bean.dto;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/25 17:12
 */
@Data
public class UserIdDTO {

    @Transient
    private BigInteger userId;
}
