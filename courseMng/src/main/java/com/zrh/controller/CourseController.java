package com.zrh.controller;

import com.zrh.dto.CourseParam;
import com.zrh.entity.Course;
import com.zrh.service.CourseService;
import com.zrh.utils.PagingResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sms/course")
@Api(tags = "课程相关")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/getCourseList")
    @ApiOperation("查询课程信息")
    private PagingResult<Course> getCourseList(@ApiParam @RequestParam String code,
                                               @RequestParam(required = false, name = "$limit", defaultValue = "10") Integer limit,
                                               @RequestParam(required = false, name = "$offset", defaultValue = "0") Integer offset) {
        return courseService.getCourseList(code, offset, limit);
    }

    @GetMapping("/getCourseByMap")
    @ApiOperation("根据专业、学期获取课程列表")
    private List<Course> getCourseList(CourseParam courseParam) {
        return courseService.getCourseList(courseParam);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除课程信息")
    public void delete(@PathVariable("ids") List<String> ids) {
        courseService.delete(ids);
    }

    @PutMapping
    @ApiOperation("修改课程信息")
    public void update(@RequestBody Course course) {
        courseService.update(course);
    }

    @PostMapping
    @ApiOperation(" 新增课程信息")
    public void addCourse(@RequestBody Course course) {
        courseService.addCourse(course);
    }
}
