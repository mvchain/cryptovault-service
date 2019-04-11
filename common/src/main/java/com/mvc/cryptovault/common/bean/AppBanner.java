package com.mvc.cryptovault.common.bean;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/4/11 21:17
 */
@Data
public class AppBanner {

    @Id
    private BigInteger id;
    private String title;
    private String content;
    private Long createdAt;

}
