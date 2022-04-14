package com.mwt.service.common;

public interface TempService {
    String save(Object value);

    int save(String key, Object value);

    Object get(String id);

    Object getByKey(String key);

    void delete(String id);

    void deleteByKey(String key);

    void update(String id, Object value);

    void updateByKey(String key, Object value);
}
