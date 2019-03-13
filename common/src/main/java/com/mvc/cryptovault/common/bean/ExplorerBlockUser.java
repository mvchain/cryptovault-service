package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/12 13:15
 */
@Data
public class ExplorerBlockUser {

    public static final double MAX_VALUE = 200000;
    @Id
    private BigInteger id;
    private String publicKey;
    private Long createdAt;

}
