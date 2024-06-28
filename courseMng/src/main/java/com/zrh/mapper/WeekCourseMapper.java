package com.zrh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zrh.entity.WeekCourse;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【week】的数据库操作Mapper
 * @createDate 2024-03-25 14:28:11
 * @Entity com.zrh.entity.WeekCourse
 */
public interface WeekCourseMapper extends BaseMapper<WeekCourse> {

    /**
     * 根据专业班级获取课程表信息
     *
     * @param profession
     * @param grade
     * @param year
     * @param term
     * @return
     */
    List<WeekCourse> getWeekCourse(String profession, Integer grade, Integer year, Integer term);
}




