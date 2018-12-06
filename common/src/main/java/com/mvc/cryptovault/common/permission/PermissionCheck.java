package com.mvc.cryptovault.common.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ignore user login check
 *
 * @author qiyichen
 * @create 2018/3/10 17:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface PermissionCheck {

    /**
     * 1	GLKZ
     * 2	冲提控制
     * 3	用户控制
     * 4	币种控制
     * 5	众筹控制
     * 6	交易控制
     * @return
     */
    String value();

}
