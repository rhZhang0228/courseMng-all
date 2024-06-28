package com.zrh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zrh.dto.TimetableQueryParam;
import com.zrh.entity.CourseInfo;
import com.zrh.entity.Timetable;
import com.zrh.entity.WeekCourse;

import java.util.List;

/**
 * @author rhZhang
 * @description 针对表【timetable】的数据库操作Service
 * @createDate 2024-03-30 17:16:19
 */
public interface TimetableService extends IService<Timetable> {
    /**
     * 学生用户获取课程表
     *
     * @param timetableQueryParam
     * @return
     */
    List<WeekCourse> getTimetableByStudent(TimetableQueryParam timetableQueryParam);

    /**
     * 教师用户获取课程表
     *
     * @param timetableQueryParam
     * @return
     */
    List<WeekCourse> getTimetableByTeacher(TimetableQueryParam timetableQueryParam);

    /**
     * 修改课程具体安排（周数等）
     *
     * @param courseInfo
     */
    void updateCourseInfo(CourseInfo courseInfo);

    /**
     * 管理员用户获取课程表
     *
     * @param timetableQueryParam
     * @return
     */
    List<WeekCourse> getTimetable(TimetableQueryParam timetableQueryParam);

    /**
     * 新增课程表
     *
     * @param weekCourseList
     */
    void add(List<WeekCourse> weekCourseList);
}
