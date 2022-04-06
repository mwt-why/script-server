package com.mwt.service.task;

import com.mwt.beans.task.AccountTaskForHistory;

/**
 * 这个接口主要是接口账号的历史问题，比如多余的友好信息
 */
public interface HistoryTaskService {

    void init();

    AccountTaskForHistory getTask();

    void deleteTask(String id);
}
