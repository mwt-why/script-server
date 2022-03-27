package com.mwt.service.res.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.mwt.beans.comm.MiniAccount;
import com.mwt.beans.res.Account;
import com.mwt.service.res.AccountService;
import com.mwt.repository.res.AccountRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Override
    public void add(Account bean) {
    }

    @Override
    public void update(Account bean) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public Page<Account> list(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    @Override
    public void upload(InputStream inputStream) {
        EasyExcel.read(inputStream, MiniAccount.class, new PageReadListener<MiniAccount>(dataList -> {
            List<Account> accounts = new ArrayList<>();
            for (MiniAccount ma : dataList) {
                Account account = new Account();
                BeanUtils.copyProperties(ma, account);
                if (!ma.getAccount().contains("@")) {   //判断是否是邮箱
                    account.setType(1);
                }
                account.setLastUpdateTime(LocalDateTime.now());
                accounts.add(account);
            }
            accountRepository.saveAll(accounts);
        })).sheet().doRead();
    }
}
