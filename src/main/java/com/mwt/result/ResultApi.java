package com.mwt.result;

public interface ResultApi<T> {
    Result<T> success(String code, String message);

    Result<T> success(String code, String message, T data);

    Result<T> failed(String code, String message);

    Result<T> failed(String code, String message, T data);
}
