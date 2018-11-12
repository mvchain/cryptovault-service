package com.mvc.cryptovault.common.util;

public class StringHelper {
    public StringHelper() {
    }

    public static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }
}