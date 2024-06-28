package com.zrh.mapper;

import com.zrh.dto.CourseParam;
import com.zrh.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【course】的数据库操作Mapper
 * @createDate 2024-03-27 12:53:03
 * @Entity com.zrh.entity.Course
 */
public interface CourseMapper extends BaseMapper<Course> {
    /**
     * 根据专业、学期获取课程列表
     *
     * @param courseParam
     * @return
     */
    List<Course> getCourseList(@Param("courseParam") CourseParam courseParam);
}




