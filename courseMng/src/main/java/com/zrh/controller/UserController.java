package com.zrh.controller;

import com.zrh.dto.UpdatePasswordDto;
import com.zrh.dto.User;
import com.zrh.service.UserService;
import com.zrh.utils.PassToken;
import com.zrh.utils.RedisConstant;
import com.zrh.vo.Tree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sms/user")
@Api(tags = "用户相关")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("login")
    @PassToken
    @ApiOperation("登录接口")
    public User getUserInfo(@ApiParam @RequestParam String username,
                            @ApiParam @RequestParam String password,
                            @ApiParam @RequestParam String level) {
        log.debug("正在登陆");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(level)) {
            log.error("参数不能为空");
            return new User();
        }
        User user = userService.getUserInfo(username, password, level);
        String token = userService.getToken(user);
        String refreshToken = userService.getRefreshToken(user);
        user.setToken(token);
        user.setRefreshToken(refreshToken);
        user.setPassword("");
        return user;
    }

    @PassToken
    @ApiOperation("是否为深色模式")
    @GetMapping("/getSilent")
    @Cacheable(cacheNames = RedisConstant.SILENT)
    public boolean getSilent() {
        return userService.getSilent();
    }

    @ApiOperation("更改深色模式")
    @PutMapping("/setSilent/{state}")
    @CacheEvict(cacheNames = RedisConstant.SILENT, allEntries = true)
    public boolean setSilent(@PathVariable("state") Integer state) {
        return userService.setSilent(state);
    }

    @GetMapping("/edit/password")
    @ApiOperation("修改密码")
    public boolean updatePassword(UpdatePasswordDto updatePasswordDto) {
        return userService.updatePassword(updatePasswordDto);
    }

    @GetMapping("/getTree")
    @ApiOperation("获取树形列表")
    @Cacheable(cacheNames = RedisConstant.TREE)
    public List<Tree> getTree() {
        return userService.getTree();
    }
}
