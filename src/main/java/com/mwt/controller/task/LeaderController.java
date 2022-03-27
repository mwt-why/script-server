package com.mwt.controller.task;

import com.mwt.beans.task.Leader;
import com.mwt.result.ApiUtil;
import com.mwt.service.task.LeaderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/leader")
public class LeaderController {

    @Resource
    private LeaderService leaderService;

    @GetMapping("/{leaderId}")
    public Object switchStatus(@PathVariable String leaderId,
                               @RequestParam int status) {
        leaderService.switchStatus(leaderId, status);
        return ApiUtil.success();
    }

    @GetMapping
    public Object upset(@RequestParam String leaderId) {
        leaderService.upset(leaderId);
        return ApiUtil.success();
    }

    @GetMapping("/leaderIsDoing")
    public Object leaderIsDoing(@RequestParam String leaderId) {
        return ApiUtil.success(leaderService.leaderIsDoing(leaderId));
    }

}
