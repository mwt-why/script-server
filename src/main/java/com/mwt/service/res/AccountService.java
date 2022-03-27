package com.mwt.service.res;

import com.mwt.beans.res.Account;
import com.mwt.service.IService;

import java.io.InputStream;

public interface AccountService extends IService<Account> {

    void upload(InputStream inputStream);
}
