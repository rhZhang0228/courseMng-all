package com.zrh.service;

import com.zrh.dto.TeacherCourseInfoParam;
import com.zrh.entity.TeacherCourse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zrh.vo.AdminProfessionInfoVo;
import com.zrh.vo.ProfessionInfoVo;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【teacher_course】的数据库操作Service
 * @createDate 2024-03-25 14:10:52
 */
public interface TeacherCourseService extends IService<TeacherCourse> {
    /**
     * 根据教师id获取教师用户所管理的专业、班级、课程名等信息
     *
     * @param teacherId
     * @return
     */
    List<ProfessionInfoVo> getProfessionInfo(String teacherId);

    /**
     * 管理员用户获取所有的班级和课程，渲染成下拉框选择
     *
     * @return
     */
    List<AdminProfessionInfoVo> getProfessionInfoByAdmin();

    /**
     * 课程表中获取课程详细信息
     *
     * @param teacherCourseInfoParam
     * @return
     */
    TeacherCourse getCourseInfo(TeacherCourseInfoParam teacherCourseInfoParam);

    /**
     * 根据教师id获取教师课程信息
     *
     * @param id
     * @return
     */
    List<TeacherCourse> getCourseListById(String id);

    /**
     * 新增
     *
     * @param teacherCourseList
     */
    void add(List<TeacherCourse> teacherCourseList);

    void editById(TeacherCourse teacherCourse);

    void deleteByIds(List<Integer> ids);
}
