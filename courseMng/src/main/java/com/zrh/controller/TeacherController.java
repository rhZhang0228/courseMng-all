package com.zrh.controller;

import com.zrh.dto.User;
import com.zrh.entity.Course;
import com.zrh.entity.WeekCourse;
import com.zrh.service.TeacherService;
import com.zrh.utils.PagingResult;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sms/user/teacher")
@Api(tags = "老师相关")
@Slf4j
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private RedisTemplate<String, User> redisTemplate;
    @Autowired
    private RedisTemplate<String, Course> courseRedisTemplate;
    @Autowired
    private RedisTemplate<String, WeekCourse> weekCourseRedisTemplate;

    @GetMapping("/getTeacherList")
    public PagingResult<User> getTeacherList(@RequestParam(required = false) @ApiParam String code,
                                             @RequestParam(required = false) @ApiParam String profession,
                                             @RequestParam(required = false) @ApiParam String grade,
                                             @RequestParam(required = false, name = "$limit", defaultValue = "10") Integer limit,
                                             @RequestParam(required = false, name = "$offset", defaultValue = "0") Integer offset) {
        return teacherService.getTeacherList(code, profession, grade, offset, limit);
    }

    @DeleteMapping("/{ids}")
    public void delete(@PathVariable("ids") List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.error("错误，ids为空");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.TEACHER_LIST);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_MAP);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_TEACHER);

        teacherService.removeByIds(ids);
    }

    @PutMapping
    public void update(@RequestBody User user) {
        teacherService.update(user);
    }

    @PostMapping
    public void addTeacher(@RequestBody User user) {
        teacherService.addTeacher(user);
    }
}
