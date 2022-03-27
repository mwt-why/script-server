package com.mwt.repository;

import com.mwt.beans.comm.Seq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;

@Component
public class AutoIdGenerator {

    @Resource
    private MongoTemplate mongoTemplate;

    @Value("${spring.data.mongodb.database}")
    private String collection;

    public int getNextSeq(Class<? extends Seq> clazz) {
        Seq seq = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(collection)),
                new Update().inc("seq", 1),
                options().upsert(true).returnNew(true),
                clazz
        );
        return seq.getSeq();
    }

}
