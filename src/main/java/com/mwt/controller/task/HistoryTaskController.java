package com.mwt.controller.task;

import com.mwt.beans.task.AccountTaskForHistory;
import com.mwt.result.ApiUtil;
import com.mwt.service.task.HistoryTaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/historyTask")
public class HistoryTaskController {

    @Resource
    private HistoryTaskService historyTaskService;

    @GetMapping
    public Object getTask() {
        AccountTaskForHistory task = historyTaskService.getTask();
        return ApiUtil.success(task);
    }

    @GetMapping(value = "/delete")
    public Object deleteTask(@RequestParam String id) {
        historyTaskService.deleteTask(id);
        return ApiUtil.success();
    }
}
