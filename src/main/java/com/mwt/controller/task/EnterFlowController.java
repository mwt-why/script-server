package com.mwt.controller.task;

import com.mwt.beans.task.EnterFlow;
import com.mwt.result.ApiUtil;
import com.mwt.service.task.EnterFlowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "enterFlow")
public class EnterFlowController {

    @Resource
    private EnterFlowService enterFlowService;

    @GetMapping
    public Object findOneByDeviceId(@RequestParam int deviceId) {
        return ApiUtil.success(enterFlowService.findOneByDeviceId(deviceId));
    }

    @PostMapping
    public Object save(@RequestBody EnterFlow enterFlow) {
        enterFlowService.save(enterFlow);
        return ApiUtil.success();
    }

    @PutMapping(value = "/updateByDeviceId")
    public Object updateByDeviceId(@RequestParam int deviceId,
                                   @RequestBody EnterFlow enterFlow) {
        enterFlowService.updateByDeviceId(deviceId, enterFlow);
        return ApiUtil.success();
    }
}
