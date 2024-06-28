package com.zrh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.dto.User;
import com.zrh.entity.Course;
import com.zrh.entity.Teacher;
import com.zrh.entity.WeekCourse;
import com.zrh.mapper.TeacherMapper;
import com.zrh.service.TeacherService;
import com.zrh.utils.PagingResult;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangronghao
 * @description 针对表【teacher】的数据库操作Service实现
 * @createDate 2024-03-25 16:42:34
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
        implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private RedisTemplate<String, User> redisTemplate;
    @Autowired
    private RedisTemplate<String, Course> courseRedisTemplate;
    @Autowired
    private RedisTemplate<String, WeekCourse> weekCourseRedisTemplate;

    @Override
    public PagingResult<User> getTeacherList(String code, String profession, String grade, Integer offset, Integer limit) {
        StringBuilder keySb = new StringBuilder();
        keySb.append(RedisConstant.TEACHER_LIST);
        if (StringUtils.isNotEmpty(code)) {
            keySb.append("::").append(code);
        }
        if (StringUtils.isNotEmpty(profession)) {
            keySb.append("::").append(profession);
        }
        if (StringUtils.isNotEmpty(grade)) {
            keySb.append("::").append(grade);
        }
        String key = keySb.toString();
        //从缓存取数据
        if (redisTemplate.hasKey(key)) {
            return RedisUtils.selectPage(redisTemplate, key, offset, limit);
        }

        Integer currentPage = offset / limit + 1;
        Page<Teacher> page = new Page<>(currentPage, limit);
        page.setOptimizeCountSql(false);
//        page.setOptimizeJoinOfCountSql(false);
        page = teacherMapper.getTeacherList(code, profession, grade, page);
        PagingResult<User> res = new PagingResult<>();
        List<User> userList = page.getRecords().stream().map(teacher -> {
            User user = new User();
            BeanUtil.copyProperties(teacher, user);
            return user;
        }).collect(Collectors.toList());
        res.setTotalCount(page.getTotal());
        res.setItems(userList);

        //加入缓存
        RedisUtils.insertList(redisTemplate, teacherMapper.getTeacherList(code, profession, grade)
                .stream().map(student -> {
                    User user = new User();
                    BeanUtil.copyProperties(student, user);
                    return user;
                }).collect(Collectors.toList()), key);
        return res;
    }

    @Override
    public void update(User user) {
        if (null == user) {
            log.error("user为空，更新失败");
            return;
        }
        Teacher teacher = new Teacher();
        BeanUtil.copyProperties(user, teacher);
        if (null == teacher.getId()) {
            log.error("user的Id为空，更新失败");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.TEACHER_LIST);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_MAP);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_TEACHER);

        teacherMapper.updateById(teacher);
    }

    @Override
    public void addTeacher(User user) {
        if (null == user) {
            log.error("user不能为空");
            return;
        }
        if (null == user.getSex()) {
            log.error("user的性别不能为空");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.TEACHER_LIST);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_MAP);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_TEACHER);

        Integer num = teacherMapper.selectCount(null).intValue();
        String str = "";
        if (num < 10) {
            str = "00" + num;
        } else if (num < 100) {
            str = "0" + num;
        } else {
            str = Integer.toString(num);
        }
        String no = RandomUtil.randomNumbers(3) + str + user.getSex().toString();
        user.setUsername(no);
        user.setId(no);
        user.setPassword("123456");
        Teacher teacher = new Teacher();
        BeanUtil.copyProperties(user, teacher);
        teacherMapper.insert(teacher);
    }


}




