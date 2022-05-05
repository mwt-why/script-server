package com.mwt.service.task.impl;

import com.mwt.beans.task.Enter;
import com.mwt.service.task.EnterService;
import com.mwt.utils.DbUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class EnterServiceImpl implements EnterService {

    private static final String COLLECTION_NAME = "enter";

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void add(Enter enter) {
        enter.setCreateTime(LocalDateTime.now());
        enter.setLastUpdateTime(LocalDateTime.now());
        mongoTemplate.save(enter, COLLECTION_NAME);
    }

    @Override
    public Enter findOneByDeviceId(int deviceId) {
        Query query = DbUtil.getQuery("deviceId", deviceId);
        return mongoTemplate.findOne(query, Enter.class, COLLECTION_NAME);
    }

    @Override
    public void update(String id, Enter enter) {
        Query query = DbUtil.getQueryById(id);
        UpdateDefinition update = DbUtil.getUpdate(enter, Arrays.asList("id"));
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }

    @Override
    public void updateByDeviceId(int deviceId, Enter enter) {
        Query query = DbUtil.getQuery("deviceId", deviceId);
        UpdateDefinition update = DbUtil.getUpdate(enter);
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }

    @Override
    public void deleteExpiring() {
        Query query = DbUtil.getQuery("status", 1);
        mongoTemplate.remove(query, COLLECTION_NAME);
    }
}
