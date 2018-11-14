package com.mvc.cryptovault.common.util;

import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;

/**
 * @author qiyichen
 * @create 2018/9/19 15:48
 */
public class ConditionUtil {

    public static void andCondition(Example.Criteria condition, String key, Object value) {
        if (value instanceof String) {
            String str = (String) value;
            if (StringUtils.isNotBlank(str)) {
                condition.andCondition(key, value);
            }
        } else {
            if (null != value) {
                condition.andCondition(key, value);
            }
        }
    }

    public static void andCondition(Example.Criteria condition, String key) {
        condition.andCondition(key);
    }
}

