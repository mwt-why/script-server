package com.mwt.beans.comm;

import lombok.Data;

@Data
public class SimpleMap<T> {

    private String key;

    private T value;

    public void put(String key, T value) {
        this.key = key;
        this.value = value;
    }
}
