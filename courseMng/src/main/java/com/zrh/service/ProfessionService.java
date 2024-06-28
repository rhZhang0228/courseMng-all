package com.zrh.service;

import com.zrh.entity.Profession;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【profession】的数据库操作Service
 * @createDate 2024-03-25 13:58:04
 */
public interface ProfessionService extends IService<Profession> {
    /**
     * 获取专业列表
     *
     * @return
     */
    List<Profession> getProfessionList();
}
