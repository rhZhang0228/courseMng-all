package com.zrh.controller;

import com.zrh.dto.User;
import com.zrh.entity.Course;
import com.zrh.entity.WeekCourse;
import com.zrh.service.StudentService;
import com.zrh.utils.PagingResult;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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
@RequestMapping("/api/sms/user/student")
@Api(tags = "学生相关")
@Slf4j
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private RedisTemplate<String, User> redisTemplate;
    @Autowired
    private RedisTemplate<String, Course> courseRedisTemplate;
    @Autowired
    private RedisTemplate<String, WeekCourse> weekCourseRedisTemplate;

    @GetMapping("/getStudentList")
    @ApiOperation("获取学生账号信息列表")
    public PagingResult<User> getStudentList(@RequestParam(required = false) @ApiParam String code,
                                             @RequestParam(required = false) @ApiParam String profession,
                                             @RequestParam(required = false) @ApiParam String grade,
                                             @RequestParam(required = false, name = "$limit", defaultValue = "10") Integer limit,
                                             @RequestParam(required = false, name = "$offset", defaultValue = "0") Integer offset) {
        return studentService.getStudentList(code, profession, grade, limit, offset);
    }

    @PostMapping
    @CacheEvict(cacheNames = RedisConstant.TREE, allEntries = true)
    public void addStudent(@RequestBody User user) {
        studentService.addStudent(user);
    }

    @DeleteMapping("/{ids}")
    @CacheEvict(cacheNames = RedisConstant.TREE, allEntries = true)
    public void delete(@PathVariable("ids") List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.error("错误，ids为空");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.STUDENT_LIST);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_SCORE);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_EXPORT);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_STUDENT);

        studentService.removeByIds(ids);
    }

    @PutMapping
    @CacheEvict(cacheNames = RedisConstant.TREE, allEntries = true)
    public void update(@RequestBody User user) {
        studentService.update(user);
    }
}
