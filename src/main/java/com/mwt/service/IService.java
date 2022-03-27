package com.mwt.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IService<T> {

    void add(T bean);

    void update(T bean);

    void delete(String id);

    Page<T> list(Pageable pageable);
}
