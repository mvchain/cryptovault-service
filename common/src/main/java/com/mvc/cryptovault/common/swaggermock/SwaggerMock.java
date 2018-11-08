package com.mvc.cryptovault.common.swaggermock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * swagger mock annotation
 *
 * @author qiyichen
 * @create 2018/3/10 17:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface SwaggerMock {

    /**
     * return json str
     *
     * @return
     */
    String value() default "";

}
