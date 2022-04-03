package com.mwt.service.task.impl;

import com.mwt.beans.task.TaskRuntime;
import com.mwt.service.task.TaskRuntimeService;
import com.mwt.utils.DbUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

@Service
public class TaskRuntimeServiceImpl implements TaskRuntimeService {

    private static final String TASK_RUN_TIME_COLLECTION = "taskRunTime";

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public void add(TaskRuntime bean) {
        mongoTemplate.save(bean, TASK_RUN_TIME_COLLECTION);
    }

    @Override
    public void update(TaskRuntime bean) {
        String taskId = bean.getTaskId();
        Query query = DbUtil.getQuery("taskId", taskId);
        UpdateDefinition update = DbUtil.getUpdate(bean, Arrays.asList("taskId"));
        mongoTemplate.updateFirst(query, update, TASK_RUN_TIME_COLLECTION);
    }


    @Override
    public void delete(String id) {
        Query query = DbUtil.getQueryById(id);
        mongoTemplate.remove(query, TASK_RUN_TIME_COLLECTION);
    }

    @Override
    public Page<TaskRuntime> list(Pageable pageable) {
        return null;
    }
}
