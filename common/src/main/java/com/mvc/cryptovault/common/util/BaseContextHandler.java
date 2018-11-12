package com.mvc.cryptovault.common.util;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class BaseContextHandler {
    public static final String ADDR_LISTEN_ = "ADDR_LISTEN_";
    public static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal();

    public BaseContextHandler() {
    }

    public static void set(String key, Object value) {
        Map<String, Object> map = (Map)threadLocal.get();
        if (map == null) {
            map = new HashMap(1);
            threadLocal.set(map);
        }

        ((Map)map).put(key, value);
    }

    public static Object get(String key) {
        Map<String, Object> map = (Map)threadLocal.get();
        if (map == null) {
            map = new HashMap(1);
            threadLocal.set(map);
        }

        return ((Map)map).get(key);
    }

    public static String getUserID() {
        Object value = get("currentUserId");
        return returnObjectValue(value);
    }

    public static BigInteger getUserIDInt() {
        Object value = get("currentUserId");
        return new BigInteger(returnObjectValue(value));
    }

    public static String getUsername() {
        Object value = get("currentUserName");
        return returnObjectValue(value);
    }

    public static String getAddress() {
        Object value = get("currentUserAddress");
        return returnObjectValue(value);
    }

    public static String getName() {
        Object value = get("currentUser");
        return StringHelper.getObjectValue(value);
    }

    public static String getToken() {
        Object value = get("currentUserToken");
        return StringHelper.getObjectValue(value);
    }

    public static void setToken(String token) {
        set("currentUserToken", token);
    }

    public static void setName(String name) {
        set("currentUser", name);
    }

    public static void setUserID(String userID) {
        set("currentUserId", userID);
    }

    public static void setUsername(String username) {
        set("currentUserName", username);
    }

    public static void setAddress(String address) {
        set("currentUserAddress", address);
    }

    private static String returnObjectValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
