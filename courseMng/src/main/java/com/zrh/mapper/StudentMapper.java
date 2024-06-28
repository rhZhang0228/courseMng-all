package com.zrh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zrh.dto.CourseListQueryParam;
import com.zrh.dto.ScoreLevelNumDto;
import com.zrh.entity.Course;
import com.zrh.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【student】的数据库操作Mapper
 * @createDate 2024-03-25 16:42:16
 * @Entity com.zrh.entity.Student
 */
public interface StudentMapper extends BaseMapper<Student> {
    /**
     * 学生用户获取导出成绩列表
     *
     * @param scoreLevelNumDto
     * @return
     */
    List<Course> getExportList(@Param("scoreLevelNumDto") ScoreLevelNumDto scoreLevelNumDto);

    /**
     * 学生用户根据学生id获取所有分数信息
     *
     * @param courseListQueryParam
     * @param page
     * @return
     */
    Page<Course> getCourseList(@Param("courseListQueryParam") CourseListQueryParam courseListQueryParam, Page<Course> page);

    /**
     * 学生用户根据学生id获取所有分数信息
     *
     * @param courseListQueryParam
     * @return
     */
    List<Course> getCourseList(@Param("courseListQueryParam") CourseListQueryParam courseListQueryParam);
}




