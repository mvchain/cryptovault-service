package com.mvc.cryptovault.common.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * request result object
 *
 * @author qiyichen
 * @create 2018/11/5 17:27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private int code;
    private String message;
    private T data;

    public Result(T data) {
        this.data = data;
        this.code = HttpStatus.OK.value();
    }

}
