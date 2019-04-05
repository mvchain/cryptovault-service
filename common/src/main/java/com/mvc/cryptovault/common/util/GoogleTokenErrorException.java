package com.mvc.cryptovault.common.util;

public class GoogleTokenErrorException extends BaseException {
    public GoogleTokenErrorException(String message) {
        super(message, 407);
    }

    public GoogleTokenErrorException(String message, int status) {
        super(message, status);
    }
}