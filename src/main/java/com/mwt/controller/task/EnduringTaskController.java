package com.mwt.controller.task;

import com.mwt.beans.comm.SimpleMap;
import com.mwt.beans.res.Device;
import com.mwt.beans.task.EnduringTask;
import com.mwt.result.ApiUtil;
import com.mwt.service.task.EnduringTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/enduringTask")
@Slf4j
public class EnduringTaskController {

    @Resource
    private EnduringTaskService enduringTask;

    @GetMapping
    public Object list(@RequestParam int page, @RequestParam int size) {
        return ApiUtil.success(enduringTask.list(PageRequest.of(page - 1, size, Sort.by("deviceId"))));
    }

    @GetMapping("/reset")
    public Object reset(@RequestParam String id) {
        enduringTask.reset(id);
        return ApiUtil.success();
    }

    @GetMapping("/getEnduringTaskDeviceId")
    public Object getEnduringTask(@RequestParam String deviceId) {
        EnduringTask task = enduringTask.getTask(deviceId);
        return ApiUtil.success(task);
    }

    @GetMapping("/getEnduringTaskById")
    public Object getEnduringTaskById(@RequestParam String id) {
        return ApiUtil.success(enduringTask.getEnduringTaskById(id));
    }

    @GetMapping("/begin")
    public Object begin(@RequestParam String id) {
        long result = enduringTask.begin(id);
        return ApiUtil.success(result);
    }

    @GetMapping("/finish")
    public Object finish(@RequestParam String id) {
        long result = enduringTask.finish(id);
        return ApiUtil.success(result);
    }

    @GetMapping("/resetAccountRole")
    public Object resetAccountRole(@RequestParam String id) {
        long result = enduringTask.resetAccountRole(id);
        return ApiUtil.success(result);
    }

    @GetMapping("/addFriend")
    public Object addFriends(@RequestParam String id) {
        List<String> friends = enduringTask.addFriend(id);
        return ApiUtil.success(friends);
    }

    @GetMapping("/reportAddFriend")
    public Object reportAddFriend(@RequestParam("id") String id,
                                  @RequestParam("leaderId") String leaderId) {
        log.info("任务:" + id + "反馈添加好友完毕");
        enduringTask.reportAddFriend(leaderId, id);
        return ApiUtil.success();
    }

    @GetMapping("/checkLeaderAddFriendOver")
    public Object checkLeaderAddFriendOver(@RequestParam("id") String id,
                                           @RequestParam("leaderId") String leaderId) {
        log.info("任务:" + id + "检查队长是否添加好友完毕");
        return ApiUtil.success(enduringTask.checkLeaderAddFriendOver(leaderId));
    }

    @GetMapping("/teamUp")
    public Object teamUp(@RequestParam String id) {
        return ApiUtil.success(enduringTask.teamUp(id));
    }

    @GetMapping("/{id}")
    public Object deleteEnduringTask(@PathVariable String id) {
        int result = enduringTask.deleteTask(id);
        return ApiUtil.success(result);
    }

    @PutMapping("/{id}")
    public Object updateEnduringTask(@PathVariable String id,
                                     @RequestBody SimpleMap simpleMap) {
        long result = enduringTask.updateTask(id, simpleMap);
        return ApiUtil.success(result);
    }

    @PutMapping("/{id}/account")
    public Object updateCurAccount(@PathVariable String id,
                                   @RequestBody SimpleMap simpleMap) {
        long result = enduringTask.updateCurAccount(id, simpleMap);
        return ApiUtil.success(result);
    }


    @PutMapping("/{id}/role")
    public Object updateCurRole(@PathVariable String id,
                                @RequestBody SimpleMap simpleMap) {
        long result = enduringTask.updateCurRole(id, simpleMap);
        return ApiUtil.success(result);
    }
}
