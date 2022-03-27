package com.mwt.repository.task;

import com.mwt.beans.task.EnduringTask;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EnduringTaskRepository extends MongoRepository<EnduringTask, String> {

}
