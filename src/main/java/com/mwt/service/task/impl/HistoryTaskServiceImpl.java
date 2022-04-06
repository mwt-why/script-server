package com.mwt.service.task.impl;

import com.mwt.beans.comm.SimpleMap;
import com.mwt.beans.res.Account;
import com.mwt.beans.task.AccountTaskForHistory;
import com.mwt.service.task.HistoryTaskService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class HistoryTaskServiceImpl implements HistoryTaskService {

    @Resource
    private MongoTemplate mongoTemplate;

    private static final String COLLECTION_NAME = "accountTaskForHistory";

    @Override
    public void init() {
        List<Account> allAccount = mongoTemplate.findAll(Account.class);
        List<AccountTaskForHistory> allTask = mongoTemplate.findAll(AccountTaskForHistory.class);
        if (Objects.isNull(allTask) || allTask.isEmpty()) {
            for (Account a : allAccount) {
                AccountTaskForHistory task = new AccountTaskForHistory();
                task.setAccount(a.getAccount());
                task.setPassword(a.getPassword());
                task.setStatus(0);
                mongoTemplate.save(task, COLLECTION_NAME);
            }
        }
    }

    private AccountTaskForHistory getAvailable() {
        Query query = Query.query(Criteria.where("status").is(0));
        return mongoTemplate.findOne(query, AccountTaskForHistory.class, COLLECTION_NAME);
    }

    private void switchStatus(String id, int status) {
        SimpleMap simpleMap = new SimpleMap<>();
        simpleMap.put("status", status);
        updateById(id, simpleMap);
    }

    @Override
    public synchronized AccountTaskForHistory getTask() {
        AccountTaskForHistory task = getAvailable();
        if (Objects.isNull(task)) {
            init();
            task = getAvailable();
        }
        if (Objects.isNull(task)) {
            return null;
        }
        switchStatus(task.getId(), 1);
        return task;
    }

    private void updateById(String id, SimpleMap<Object> simpleMap) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update().set(simpleMap.getKey(), simpleMap.getValue());
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }

    @Override
    public void deleteTask(String id) {
        switchStatus(id, -1);
    }
}
