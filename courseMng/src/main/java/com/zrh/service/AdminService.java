package com.zrh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrh.dto.User;
import com.zrh.entity.Admin;
import com.zrh.utils.PagingResult;

/**
 * @author zhangronghao
 * @description 针对表【admin】的数据库操作Service
 * @createDate 2024-03-25 16:37:57
 */
public interface AdminService extends IService<Admin> {
    /**
     * 获取管理员账号信息列表
     *
     * @param code
     * @param limit
     * @param offset
     * @return
     */
    PagingResult<User> getAdminList(String code, Integer limit, Integer offset);

    /**
     * 新增
     *
     * @param user
     */
    void add(User user);

    /**
     * 更新
     *
     * @param user
     */
    void upadte(User user);

    /**
     * 发送成绩单
     */
    void sendTranscript();
}
