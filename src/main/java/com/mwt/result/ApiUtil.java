package com.mwt.result;

import com.mwt.result.impl.ResultApiImpl;

public class ApiUtil {

    private ApiUtil() {
    }

    public static <T> Result success() {
        ResultApi<T> resultApi = new ResultApiImpl();
        return resultApi.success(ResultCode.SUCCESS.code(), ResultCode.SUCCESS.message());
    }

    public static <T> Result success(T data) {
        ResultApi<T> resultApi = new ResultApiImpl();
        return resultApi.success(ResultCode.SUCCESS.code(), ResultCode.SUCCESS.message(), data);
    }

    public static <T> Result success(T data, ResultCode resultCode) {
        ResultApi<T> resultApi = new ResultApiImpl<>();
        return resultApi.success(resultCode.code(), resultCode.message());
    }

    public static <T> Result fail(ResultCode resultCode) {
        ResultApi resultApi = new ResultApiImpl<>();
        return resultApi.failed(resultCode.code(), resultCode.message());
    }

    public static <T> Result fail(ResultCode resultCode, T data) {
        ResultApi resultApi = new ResultApiImpl();
        return resultApi.failed(resultCode.code(), resultCode.message(), data);
    }
}
