package com.mwt.service.task.impl;

import com.mwt.beans.task.EnterFlow;
import com.mwt.service.task.EnterFlowService;
import com.mwt.utils.DbUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
public class EnterFlowServiceImpl implements EnterFlowService {

    private static final String COLLECTION_NAME = "enterFlow";

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void save(EnterFlow enterFlow) {
        enterFlow.setCreateTime(LocalDateTime.now());
        enterFlow.setLastUpdateTime(LocalDateTime.now());
        mongoTemplate.save(enterFlow, COLLECTION_NAME);
    }

    @Override
    public EnterFlow findOneByDeviceId(int deviceId) {
        Query query = DbUtil.getQuery("deviceId", deviceId);
        return mongoTemplate.findOne(query, EnterFlow.class, COLLECTION_NAME);
    }

    @Override
    public void update(String id, EnterFlow enterFlow) {
        Query query = DbUtil.getQueryById(id);
        UpdateDefinition update = DbUtil.getUpdate(enterFlow, Arrays.asList("id"));
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }

    @Override
    public void updateByDeviceId(int deviceId, EnterFlow enterFlow) {
        Query query = DbUtil.getQuery("deviceId", deviceId);
        UpdateDefinition update = DbUtil.getUpdate(enterFlow);
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }
}
