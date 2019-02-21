package com.mvc.cryptovault.common.util;

public class PassWrongMoreException extends BaseException {
    public PassWrongMoreException(String message) {
        super(message, 402);
    }

    public PassWrongMoreException(String message, int status) {
        super(message, status);
    }
}