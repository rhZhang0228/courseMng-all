package com.zrh.controller;

import com.zrh.dto.TeacherCourseInfoParam;
import com.zrh.entity.TeacherCourse;
import com.zrh.service.TeacherCourseService;
import com.zrh.vo.AdminProfessionInfoVo;
import com.zrh.vo.ProfessionInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sms/teacher/course")
@Api(tags = "教师课程相关")
@Slf4j
public class TeacherCourseController {
    @Autowired
    private TeacherCourseService teacherCourseService;

    @ApiOperation("教师的专业信息")
    @GetMapping("/getProfessionInfoByTeacher/{teacherId}")
    public List<ProfessionInfoVo> getProfessionInfo(@PathVariable("teacherId") String teacherId) {
        return teacherCourseService.getProfessionInfo(teacherId);
    }

    @GetMapping("/getProfessionInfoByAdmin")
    @ApiOperation("管理员信息")
    public List<AdminProfessionInfoVo> getProfessionInfoByAdmin() {
        return teacherCourseService.getProfessionInfoByAdmin();
    }

    @GetMapping("/getCourseInfo")
    public TeacherCourse getCourseInfo(TeacherCourseInfoParam teacherCourseInfoParam) {
        return teacherCourseService.getCourseInfo(teacherCourseInfoParam);
    }

    @ApiOperation("根据教师id获取教师课程信息")
    @GetMapping("/getCourseListById/{id}")
    public List<TeacherCourse> getCourseListById(@PathVariable("id") String id) {
        return teacherCourseService.getCourseListById(id);
    }

    @PutMapping
    @ApiOperation("更新")
    public void update(@RequestBody TeacherCourse teacherCourse) {
        if (null == teacherCourse.getId()) {
            log.error("teacherCourse的id不能为空");
        }
        teacherCourseService.editById(teacherCourse);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除")
    public void delete(@PathVariable("ids") List<Integer> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.error("ids不能为空");
        }
        teacherCourseService.deleteByIds(ids);
    }

    @PostMapping
    @ApiOperation("新增")
    public void add(@RequestBody List<TeacherCourse> teacherCourseList) {
        teacherCourseService.add(teacherCourseList);
    }
}
