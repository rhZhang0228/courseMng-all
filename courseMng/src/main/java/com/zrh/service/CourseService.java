package com.zrh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrh.dto.CourseParam;
import com.zrh.entity.Course;
import com.zrh.utils.PagingResult;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【course】的数据库操作Service
 * @createDate 2024-03-27 12:53:03
 */
public interface CourseService extends IService<Course> {
    /**
     * 查询课程信息
     *
     * @param code
     * @param offset
     * @param limit
     * @return
     */
    PagingResult<Course> getCourseList(String code, Integer offset, Integer limit);

    /**
     * 根据专业、学期获取课程列表
     *
     * @param courseParam
     * @return
     */
    List<Course> getCourseList(CourseParam courseParam);

    /**
     * 删除课程信息
     *
     * @param ids
     */
    void delete(List<String> ids);

    /**
     * 修改课程信息
     *
     * @param course
     */
    void update(Course course);

    /**
     * 新增课程信息
     *
     * @param course
     */
    void addCourse(Course course);
}
