package com.zrh.controller;

import com.zrh.entity.Profession;
import com.zrh.service.ProfessionService;
import com.zrh.utils.RedisConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sms/profession")
@Api(tags = "专业相关")
public class ProfessionController {
    @Autowired
    private ProfessionService professionService;

    @GetMapping("/getProfessionList")
    @ApiOperation("获取专业列表")
    @Cacheable(cacheNames = RedisConstant.PROFESSION_LIST)
    private List<Profession> getProfessionList() {
        return professionService.getProfessionList();
    }
}
