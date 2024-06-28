package com.zrh.controller;

import com.zrh.service.UploadService;
import com.zrh.utils.RedisConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "上传相关")
@RestController
@RequestMapping("/api/sms/upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @GetMapping("/getHeadImg")
    @ApiOperation("获取头像")
    @Cacheable(cacheNames = RedisConstant.HEAD_IMG, key = "#userId + '-' + #level")
    public String getAdminList(@ApiParam @RequestParam String userId,
                               @ApiParam @RequestParam String level) {
        return uploadService.getHeader(userId, level);
    }

    @PostMapping("/headImg")
    @CachePut(cacheNames = RedisConstant.HEAD_IMG, key = "#userId + '-' + #level")
    public String upload(MultipartFile file,
                         @ApiParam @RequestParam Integer level,
                         @ApiParam @RequestParam("id") String userId) {
        return uploadService.upload(file, level, userId);
    }

}
