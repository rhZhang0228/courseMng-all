package com.zrh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrh.dto.User;
import com.zrh.entity.Student;
import com.zrh.utils.PagingResult;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【student】的数据库操作Service
 * @createDate 2024-03-25 16:42:16
 */
public interface StudentService extends IService<Student> {
    /**
     * 获取学生账号信息列表
     *
     * @param code
     * @param profession
     * @param grade
     * @param limit
     * @param offset
     * @return
     */
    PagingResult<User> getStudentList(String code, String profession, String grade, Integer limit, Integer offset);

    /**
     * 获取所有学生列表
     *
     * @return
     */
    List<Student> getAllStudent();

    /**
     * 添加学生
     *
     * @param user
     */
    void addStudent(User user);

    void update(User user);
}
