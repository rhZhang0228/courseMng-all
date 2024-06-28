package com.zrh.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zrh.dto.TimetableQueryParam;
import com.zrh.entity.CourseInfo;
import com.zrh.entity.WeekCourse;
import com.zrh.service.TimetableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sms/timetable")
@Api(tags = "课程表")
public class TimetableController {
    @Autowired
    private TimetableService timetableService;

    @GetMapping("/getTimetableByStudent")
    public List<WeekCourse> getTimetableByStudent(TimetableQueryParam timetableQueryParam) {
        return timetableService.getTimetableByStudent(timetableQueryParam);
    }

    @GetMapping("/getTimetableByTeacher")
    public List<WeekCourse> getTimetableByTeacher(TimetableQueryParam timetableQueryParam) {
        return timetableService.getTimetableByTeacher(timetableQueryParam);
    }

    @PostMapping("/updateCourseInfo")
    public void updateCourseInfo(@RequestBody CourseInfo courseInfo) {
        timetableService.updateCourseInfo(courseInfo);
    }

    @GetMapping("/getTimetable")
    @ApiOperation("管理员用户获取课程表")
    public List<WeekCourse> getTimetable(TimetableQueryParam timetableQueryParam) {
        return timetableService.getTimetable(timetableQueryParam);
    }

    @PostMapping
    @ApiOperation("新增课程表")
    public void add(@RequestBody List<WeekCourse> weekCourseList) {
        timetableService.add(weekCourseList);
    }
}
