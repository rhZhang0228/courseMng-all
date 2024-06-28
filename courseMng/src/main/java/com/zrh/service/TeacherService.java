package com.zrh.service;

import com.zrh.dto.User;
import com.zrh.entity.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zrh.utils.PagingResult;

/**
 * @author zhangronghao
 * @description 针对表【teacher】的数据库操作Service
 * @createDate 2024-03-25 16:42:34
 */
public interface TeacherService extends IService<Teacher> {
    /**
     * 获取教师账号信息列表
     *
     * @param code
     * @param profession
     * @param grade
     * @param offset
     * @param limit
     * @return
     */
    PagingResult<User> getTeacherList(String code, String profession, String grade, Integer offset, Integer limit);

    /**
     * @param user
     */
    void update(User user);

    /**
     * @param user
     */
    void addTeacher(User user);
}
