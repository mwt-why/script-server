package com.mwt.controller.common;

import com.mwt.result.ApiUtil;
import com.mwt.service.common.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/common")
public class CommonController {

    @Resource
    private CommonService commonService;

    @GetMapping("/heartbeat")
    public Object heartbeat(@RequestParam String deviceId) {
        commonService.heartbeat(deviceId);
        return ApiUtil.success(true);
    }
}
