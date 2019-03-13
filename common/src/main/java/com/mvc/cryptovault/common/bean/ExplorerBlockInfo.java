package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/3/12 13:12
 */
@Data
public class ExplorerBlockInfo {

    @Id
    private BigInteger id;
    private Integer difficult;
    private Integer transactions;
    private Long createdAt;
    private String hash;

}
