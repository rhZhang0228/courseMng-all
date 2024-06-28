package com.zrh.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zrh.dto.CourseListQueryParam;
import com.zrh.entity.Course;
import com.zrh.dto.ScoreLevelNumDto;
import com.zrh.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【admin】的数据库操作Mapper
 * @createDate 2024-03-25 16:37:57
 * @Entity com.zrh.entity.Admin
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 管理员用户获取导出成绩列表
     *
     * @param scoreLevelNumDto
     * @return
     */
    List<Course> getExportList(@Param("scoreLevelNumDto") ScoreLevelNumDto scoreLevelNumDto);

    /**
     * 管理员用户根据专业、班级查询所有分数信息
     *
     * @param courseListQueryParam
     * @param page
     * @return
     */
    Page<Course> getCourseList(@Param("courseListQueryParam") CourseListQueryParam courseListQueryParam, Page<Course> page);

    /**
     * 管理员用户根据专业、班级查询所有分数信息
     *
     * @param courseListQueryParam
     * @return
     */
    List<Course> getCourseList(@Param("courseListQueryParam") CourseListQueryParam courseListQueryParam);
}




