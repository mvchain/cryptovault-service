package com.mvc.cryptovault.common.util;

public class TokenErrorException extends BaseException {
    public TokenErrorException(String message) {
        super(message, 403);
    }

    public TokenErrorException(String message, int status) {
        super(message, status);
    }
}