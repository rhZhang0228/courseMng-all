package com.zrh.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zrh.dto.CourseListQueryParam;
import com.zrh.entity.Course;
import com.zrh.dto.ScoreLevelNumDto;
import com.zrh.entity.Teacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【teacher】的数据库操作Mapper
 * @createDate 2024-03-25 16:42:34
 * @Entity com.zrh.entity.Teacher
 */
public interface TeacherMapper extends BaseMapper<Teacher> {
    /**
     * 教师用户获取导出成绩列表
     *
     * @param scoreLevelNumDto
     * @return
     */
    List<Course> getExportList(@Param("scoreLevelNumDto") ScoreLevelNumDto scoreLevelNumDto);

    /**
     * 获取教师账号信息列表
     *
     * @param code
     * @param profession
     * @param grade
     * @param page
     * @return
     */
    Page<Teacher> getTeacherList(@Param("code") String code,
                                 @Param("profession") String profession,
                                 @Param("grade") String grade,
                                 Page<Teacher> page);

    /**
     * 获取教师账号信息列表
     *
     * @param code
     * @param profession
     * @param grade
     * @param page
     * @return
     */
    List<Teacher> getTeacherList(@Param("code") String code,
                                 @Param("profession") String profession,
                                 @Param("grade") String grade);

    /**
     * 教师用户根据专业等获取课程信息
     *
     * @param courseListQueryParam
     * @param page
     * @return
     */
    Page<Course> getCourseList(@Param("courseListQueryParam") CourseListQueryParam courseListQueryParam, Page<Course> page);


    /**
     * 教师用户根据专业等获取课程信息
     *
     * @param courseListQueryParam
     * @return
     */
    List<Course> getCourseList(@Param("courseListQueryParam") CourseListQueryParam courseListQueryParam);

}




