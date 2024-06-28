package com.zrh.mapper;

import com.zrh.dto.TeacherCourseInfoParam;
import com.zrh.entity.TeacherCourse;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhangronghao
 * @description 针对表【teacher_course】的数据库操作Mapper
 * @createDate 2024-03-25 14:10:52
 * @Entity com.zrh.entity.TeacherCourse
 */
public interface TeacherCourseMapper extends BaseMapper<TeacherCourse> {
    /**
     * 课程表中获取课程详细信息
     *
     * @param teacherCourseInfoParam
     * @return
     */
    TeacherCourse getCourseInfo(@Param("teacherCourseInfoParam") TeacherCourseInfoParam teacherCourseInfoParam);
}




