package com.mvc.cryptovault.common.util;

import org.springframework.util.NumberUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * @author qiyichen
 * @create 2018/11/12 16:26
 */
public class SerializeUtil {

    public static <T> T list2Bean(List<String> keys, List<Object> values, Class<T> clazz) {
        if (keys.size() != values.size() || keys.size() == 0) {
            return null;
        }
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            Field[] fs = clazz.getDeclaredFields();
            for (int i = 0; i < keys.size(); i++) {
                var field = clazz.getDeclaredField(keys.get(i));
                field.set(obj, string2Type(values.get(i).toString(), field.getType()));
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T string2Type(String value, Class<T> clazz) {
        if (clazz.equals(String.class)) {
            return (T) value;
        } else if (clazz.equals(Float.class)) {
            return (T) NumberUtils.parseNumber(value, Float.class);
        } else if (clazz.equals(BigDecimal.class)) {
            return (T) NumberUtils.parseNumber(value, BigDecimal.class);
        } else if (clazz.equals(BigInteger.class)) {
            return (T) NumberUtils.parseNumber(value, BigInteger.class);
        } else if (clazz.equals(Integer.class)) {
            return (T) NumberUtils.parseNumber(value, Integer.class);
        }
        return null;
    }

    public static <T> T map2Bean(Map<Object, Object> map, Class<T> clazz) {
        if (map.size() == 0) {
            return null;
        }
        try {
            T obj = clazz.getDeclaredConstructor().newInstance();
            Field[] fs = clazz.getDeclaredFields();
            for (Map.Entry entry : map.entrySet()) {
                var field = clazz.getDeclaredField(entry.getKey().toString());
                field.set(obj, string2Type(entry.getValue().toString(), field.getType()));
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
