package com.mwt.controller.task;

import com.mwt.beans.comm.SimpleMap;
import com.mwt.beans.res.Device;
import com.mwt.beans.task.EnduringTask;
import com.mwt.result.ApiUtil;
import com.mwt.service.task.EnduringTaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/enduringTask")
public class EnduringTaskController {

    @Resource
    private EnduringTaskService enduringTask;

    @GetMapping
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

    @GetMapping("/tagCurAccountIsDo")
    public Object tagCurAccountIsDo(@RequestParam String id) {
        long result = enduringTask.tagCurAccountIsDo(id);
        return ApiUtil.success(result);
    }

    @GetMapping("/addFriends")
    public Object addFriends(@RequestParam String id) {
        List<String> friends = enduringTask.addFriends(id);
        return ApiUtil.success(friends);
    }

    @GetMapping("/teamUp")
    public Object teamUp(@RequestParam String id) {
        return ApiUtil.success(enduringTask.teamUp(id));
    }

    @GetMapping("/checkLeaderAddFriendOver")
    public Object checkLeaderAddFriendOver(@RequestParam String leaderId) {
        boolean flag = enduringTask.checkLeaderAddFriendOver(leaderId);
        return ApiUtil.success(flag);
    }

    @GetMapping("/reportAddFriend")
    public Object reportAddFriend(@RequestParam String id) {
        long result = enduringTask.reportAddFriend(id);
        return ApiUtil.success(result);
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

    @PutMapping("/device")
    public Object registerDevice(@RequestBody Device device) {
        int deviceId = enduringTask.registerDevice(device);
        return ApiUtil.success(deviceId);
    }

    @GetMapping("/device")
    public Object checkDevice(@RequestParam String serial) {
        int deviceId = enduringTask.checkDevice(serial);
        return ApiUtil.success(deviceId);
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
