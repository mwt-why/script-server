package com.mwt.controller.res;

import com.mwt.result.ApiUtil;
import com.mwt.service.res.DeviceService;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
