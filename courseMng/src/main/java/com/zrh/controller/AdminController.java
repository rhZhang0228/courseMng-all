package com.zrh.controller;

import com.zrh.dto.User;
import com.zrh.service.AdminService;
import com.zrh.utils.PagingResult;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sms/user/admin")
@Api(tags = "admin相关")
@Slf4j
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @GetMapping("/getAdminList")
    public PagingResult<User> getAdminList(@RequestParam @ApiParam String code,
                                           @RequestParam(required = false, name = "$limit", defaultValue = "10") Integer limit,
                                           @RequestParam(required = false, name = "$offset", defaultValue = "0") Integer offset) {
        return adminService.getAdminList(code, limit, offset);
    }

    @PostMapping
    public void addAdmin(@RequestBody User user) {
        adminService.add(user);
    }

    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.error("错误，ids为空");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.ADMIN_LIST);
        adminService.removeByIds(ids);
    }

    @PutMapping
    public void update(@RequestBody User user) {
        adminService.upadte(user);
    }

    @PostMapping("sendTranscript")
    @ApiOperation("给所有人发送成绩单")
    public void sendTranscript() {
        adminService.sendTranscript();
    }
}
