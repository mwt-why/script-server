package com.mwt.exception;

import com.mwt.exception.exceptions.BusinessException;
import com.mwt.result.ApiUtil;
import com.mwt.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

//@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Object businessException(HttpServletRequest request, BusinessException e) {
        log.error(e.getMessage());
        return ApiUtil.fail(ResultCode.FAIL);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object exception(HttpServletRequest request, Exception e) {
        log.error(e.getMessage());
        return ApiUtil.fail(ResultCode.FAIL);
    }

}
