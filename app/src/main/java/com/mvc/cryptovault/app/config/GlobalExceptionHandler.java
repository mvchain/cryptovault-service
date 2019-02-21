package com.mvc.cryptovault.app.config;

import com.mvc.cryptovault.common.bean.vo.Result;
import com.mvc.cryptovault.common.util.PassWrongMoreException;
import com.mvc.cryptovault.common.util.PvkeyException;
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
    @ResponseStatus(HttpStatus.OK)
    public Result loginExceptionException() {
        return new Result(HttpStatus.FORBIDDEN.value(), "token error", null);
    }

    @ExceptionHandler(TokenErrorException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result tokenErrorExceptionException() {
        return new Result(HttpStatus.UNAUTHORIZED.value(), "token error", null);
    }

    @ExceptionHandler(PassWrongMoreException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result tokenPassWrongMoreException(PassWrongMoreException e) {
        return new Result(HttpStatus.PAYMENT_REQUIRED.value(), e.getMessage(), null);
    }

    @ExceptionHandler(PvkeyException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result pvkeyExceptionException(PvkeyException e) {
        return new Result(HttpStatus.NOT_ACCEPTABLE.value(), e.getMessage(), null);
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
