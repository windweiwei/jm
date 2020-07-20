package com.jimang.advice;

import com.github.fashionbrot.validated.exception.ValidatedException;
import com.jimang.enums.ResponseEnum;
import com.jimang.response.BaseResponse;
import com.jimang.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Auther:wind
 * @Date:2020/7/20
 * @Version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidatedException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse ValidationException(ValidatedException e) {
        BaseResponse response = new BaseResponse();
        response.setCode(5000);
        response.setMsg(e.getFieldName() + ":" + e.getMsg());
        return response;
    }
}
