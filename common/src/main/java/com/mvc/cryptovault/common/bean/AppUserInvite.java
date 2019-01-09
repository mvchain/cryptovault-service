package com.mvc.cryptovault.common.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 * @author qiyichen
 * @create 2019/1/9 15:50
 */
@Data
public class AppUserInvite {

    private BigInteger userId;
    private BigInteger inviteUserId;
    private BigInteger createdAt;
    private BigInteger updatedAt;

}
