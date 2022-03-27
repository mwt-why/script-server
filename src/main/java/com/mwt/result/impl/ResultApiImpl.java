package com.mwt.result.impl;

import com.mwt.result.Result;
import com.mwt.result.ResultApi;

public class ResultApiImpl<T> implements ResultApi<T> {
    private static final byte SUCCESS = 0;

    private static final byte FAIL = 1;

    @Override
    public Result<T> success(String code, String message) {
        Result<T> result = new Result();
        result.setFlag(SUCCESS);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    @Override
    public Result<T> success(String code, String message, T data) {
        Result<T> result = new Result();
        result.setFlag(SUCCESS);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    @Override
    public Result<T> failed(String code, String message) {
        Result<T> result = new Result();
        result.setFlag(FAIL);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    @Override
    public Result<T> failed(String code, String message, T data) {
        Result<T> result = new Result();
        result.setFlag(FAIL);
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
