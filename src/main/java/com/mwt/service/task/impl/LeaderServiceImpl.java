package com.mwt.service.task.impl;

import com.mwt.beans.task.Leader;
import com.mwt.service.task.LeaderService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LeaderServiceImpl implements LeaderService {

    private static final String LEADER_COLLECTION = "leader";

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void upset(String leaderId) {
        Query query = Query.query(Criteria.where("leaderId").is(leaderId));
        Update update = new Update().set("status", 1);
        mongoTemplate.upsert(query, update, LEADER_COLLECTION);
    }

    @Override
    public void switchStatus(String leaderId, int status) {
        Query query = Query.query(Criteria.where("leaderId").is(leaderId));
        Update update = new Update().set("status", status);
        mongoTemplate.updateFirst(query, update, LEADER_COLLECTION);
    }

    @Override
    public boolean leaderIsDoing(String leaderId) {
        Query query = Query.query(Criteria.where("leaderId").is(leaderId));
        Leader leader = mongoTemplate.findOne(query, Leader.class);
        return leader.getStatus() == 1 ? true : false;
    }
}
