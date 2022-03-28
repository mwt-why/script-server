package com.mwt.controller.res;

import com.mwt.beans.res.Account;
import com.mwt.result.ApiUtil;
import com.mwt.service.res.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@RequestMapping(value = "/res/account")
@Slf4j
public class AccountController {

    @Resource
    private AccountService accountService;

    public Object add(@RequestBody Account bean) {
        accountService.add(bean);
        return ApiUtil.success();
    }

    public Object delete(@RequestParam String id) {
        return null;
    }

    public Object update(@RequestBody Account bean) {
        return null;
    }

    @GetMapping
    public Object list(@RequestParam int page,
                       @RequestParam int size) {
        Page<Account> accounts = accountService.list(PageRequest.of(page, size));
        return ApiUtil.success(accounts);
    }

    @PostMapping("/upload")
    public Object upload(@RequestPart MultipartFile file) throws IOException {
        accountService.upload(file.getInputStream());
        return ApiUtil.success();
    }
}
