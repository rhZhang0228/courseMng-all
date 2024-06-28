package com.zrh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zrh.entity.CourseInfo;

/**
 * @author zhangronghao
 * @description 针对表【course_info】的数据库操作Mapper
 * @createDate 2024-03-25 13:44:34
 * @Entity com.zrh.entity.CourseInfo
 */
public interface CourseInfoMapper extends BaseMapper<CourseInfo> {

    /**
     * 获取课程信息详情
     *
     * @param profession
     * @param courseName
     * @return
     */
    CourseInfo getCourseInfo(String profession, String courseName);
}




