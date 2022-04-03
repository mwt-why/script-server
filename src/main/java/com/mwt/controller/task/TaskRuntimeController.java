package com.mwt.controller.task;

import com.mwt.beans.task.TaskRuntime;
import com.mwt.result.ApiUtil;
import com.mwt.service.task.TaskRuntimeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/taskRuntime")
public class TaskRuntimeController {

    @Resource
    private TaskRuntimeService taskRuntimeService;

    @GetMapping
    public Object list(@RequestParam int page,
                       @RequestParam int size) {
        Page<TaskRuntime> list = taskRuntimeService.list(PageRequest.of(page, size));
        return ApiUtil.success(list);
    }

    @PostMapping
    public Object add(@RequestBody TaskRuntime taskRuntime) {
        taskRuntimeService.add(taskRuntime);
        return ApiUtil.success();
    }

    @PutMapping
    public Object delete(@RequestParam String id) {
        taskRuntimeService.delete(id);
        return ApiUtil.success();
    }
}
