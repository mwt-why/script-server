package com.mwt.service.common.impl;

import com.mwt.beans.comm.SimpleMap;
import com.mwt.beans.comm.Temp;
import com.mwt.service.common.TempService;
import com.mwt.utils.DbUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类不提供外部接口，仅供内部使用
 */
@Service
public class TempServiceImpl implements TempService {
    private static final String COLLECTION_NAME = "temp";

    @Resource
    private MongoTemplate mongoTemplate;

    private Map<String, Object> tempCache = new ConcurrentHashMap<>();

    /**
     * 自定义的key的情况
     */
    private Map<String, Object> customKeyTempCache = new ConcurrentHashMap<>();

    @Override
    public String save(Object value) {
        Temp temp = Temp.builder().
                value(value).
                build();
        Temp saved = mongoTemplate.save(temp, COLLECTION_NAME);
        String id = saved.getId();
        tempCache.put(id, value);
        return id;
    }

    @Override
    public int save(String key, Object value) {
        if (customKeyTempCache.containsKey(key)) {
            return -1;//key重复
        }
        Temp temp = Temp.builder().
                key(key).
                value(value).
                build();
        mongoTemplate.save(temp, COLLECTION_NAME);
        customKeyTempCache.put(key, value);
        return 1;
    }

    @Override
    public Object get(String id) {
        Object value = tempCache.get(id);
        if (Objects.isNull(value)) {
            Temp temp = mongoTemplate.findOne(DbUtil.getQueryById(id), Temp.class, COLLECTION_NAME);
            if (Objects.isNull(temp)) {
                return null;
            }
            value = temp.getValue();
            tempCache.put(id, value);
        }
        return value;
    }

    @Override
    public Object getByKey(String key) {
        Object value = customKeyTempCache.get(key);
        if (Objects.isNull(value)) {
            Temp temp = mongoTemplate.findOne(DbUtil.getQuery("key", key), Temp.class, COLLECTION_NAME);
            if (Objects.isNull(temp)) {
                return null;
            }
            value = temp.getValue();
            customKeyTempCache.put(key, value);
        }
        return value;
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(DbUtil.getQueryById(id), COLLECTION_NAME);
        tempCache.remove(id);
    }

    @Override
    public void deleteByKey(String key) {
        mongoTemplate.remove(DbUtil.getQuery("key", key), COLLECTION_NAME);
        customKeyTempCache.remove(key);
    }

    private void baseUpdate(String field, String key, Object value) {
        Query query = Query.query(Criteria.where(field).is(key));
        UpdateDefinition update = new Update().set("value", value);
        mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
    }

    @Override
    public void update(String id, Object value) {
        baseUpdate("_id", id, value);
    }

    @Override
    public void updateByKey(String key, Object value) {
        baseUpdate("key", key, value);
    }
}
