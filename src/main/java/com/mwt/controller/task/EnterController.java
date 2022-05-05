package com.mwt.controller.task;

import com.mwt.beans.task.Enter;
import com.mwt.result.ApiUtil;
import com.mwt.service.task.EnterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/enter")
public class EnterController {

    @Resource
    private EnterService enterService;

    @GetMapping
    public Object findOneByDeviceId(@RequestParam int deviceId) {
        return ApiUtil.success(enterService.findOneByDeviceId(deviceId));
    }

    @PostMapping
    public Object add(@RequestBody Enter enter) {
        enterService.add(enter);
        return ApiUtil.success();
    }

    @PutMapping
    public Object update(@RequestParam String id,
                         @RequestBody Enter enter) {
        enter.setLastUpdateTime(LocalDateTime.now());
        enterService.update(id, enter);
        return ApiUtil.success();
    }

    @PutMapping("/updateByDeviceId")
    public Object updateByDeviceId(@RequestParam int deviceId,
                                   @RequestBody Enter enter) {
        enter.setLastUpdateTime(LocalDateTime.now());
        enterService.updateByDeviceId(deviceId, enter);
        return ApiUtil.success();
    }
}
