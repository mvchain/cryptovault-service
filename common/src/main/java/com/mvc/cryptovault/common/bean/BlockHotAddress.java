package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2018/12/14 10:58
 */
@Data
public class BlockHotAddress {

    @Id
    private BigInteger id;
    private String address;
    private String pvKey;
}
