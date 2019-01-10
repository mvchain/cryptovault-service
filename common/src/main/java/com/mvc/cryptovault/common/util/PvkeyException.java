package com.mvc.cryptovault.common.util;

public class PvkeyException extends BaseException {
    public PvkeyException(String message) {
        super(message, 406);
    }

    public PvkeyException(String message, int status) {
        super(message, status);
    }
}