package com.mwt.controller.res;

import com.mwt.beans.res.Device;
import com.mwt.result.ApiUtil;
import com.mwt.service.res.DeviceService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/res/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @GetMapping
    public Object list(@RequestParam int page,
                       @RequestParam int size) {
        return ApiUtil.success(deviceService.list(PageRequest.of(page - 1, size)));
    }

    @PostMapping
    public Object registerDevice(@RequestBody Device device) {
        return ApiUtil.success(deviceService.save(device));
    }

    @GetMapping("/check")
    public Object checkDevice(@RequestParam String serial) {
        int deviceId = deviceService.check(serial);
        return ApiUtil.success(deviceId);
    }
}
