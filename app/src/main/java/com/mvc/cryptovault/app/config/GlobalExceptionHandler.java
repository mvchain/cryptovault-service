package com.mvc.cryptovault.app.config;

import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.util.TokenErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.security.auth.login.LoginException;

/**
 * @author qyc
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result loginExceptionException() {
        return new Result(HttpStatus.UNAUTHORIZED.value(), "token error", null);
    }

    @ExceptionHandler(TokenErrorException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result tokenErrorExceptionException() {
        return new Result(HttpStatus.FORBIDDEN.value(), "token error", null);
    }

    @ExceptionHandler(IllegalAccessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result illegalAccessExceptionException(IllegalAccessException e) {
        return new Result(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result illegalArgumentExceptionException(IllegalArgumentException e) {
        return new Result(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result methodArgumentNotValidExceptionException(MethodArgumentNotValidException e) {
        return new Result(HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), null);
    }
}
