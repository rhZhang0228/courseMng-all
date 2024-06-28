package com.zrh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.dto.User;
import com.zrh.entity.*;
import com.zrh.mapper.ProfessionMapper;
import com.zrh.mapper.StudentCourseMapper;
import com.zrh.mapper.StudentMapper;
import com.zrh.service.StudentService;
import com.zrh.utils.PagingResult;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhangronghao
 * @description 针对表【student】的数据库操作Service实现
 * @createDate 2024-03-25 16:42:16
 */
@Service
@Slf4j
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
        implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ProfessionMapper professionMapper;
    @Autowired
    private RedisTemplate<String, User> redisTemplate;
    @Autowired
    private RedisTemplate<String, Student> studentRedisTemplate;
    @Autowired
    private StudentCourseMapper studentCourseMapper;
    @Autowired
    private RedisTemplate<String, Course> courseRedisTemplate;
    @Autowired
    private RedisTemplate<String, WeekCourse> weekCourseRedisTemplate;

    @Override
    public PagingResult<User> getStudentList(String code, String profession, String grade, Integer limit, Integer offset) {
        StringBuilder keySb = new StringBuilder();
        keySb.append(RedisConstant.STUDENT_LIST);
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
        Page<Student> page = new Page<>(currentPage, limit);
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(code)) {
            wrapper.like(Student::getId, code).or().like(Student::getRealName, code)
                    .or().like(Student::getProfession, code);
        }
        wrapper.eq(StringUtils.isNotEmpty(profession), Student::getProfession, profession)
                .eq(StringUtils.isNotEmpty(grade), Student::getGrade, grade).orderByDesc(Student::getId);
        page = studentMapper.selectPage(page, wrapper);
        List<User> userList = page.getRecords().stream().map(student -> {
            User user = new User();
            BeanUtil.copyProperties(student, user);
            return user;
        }).collect(Collectors.toList());
        PagingResult<User> res = new PagingResult<>(page.getTotal(), userList);

        //加入缓存
        RedisUtils.insertList(redisTemplate, studentMapper.selectList(wrapper).stream().map(student -> {
            User user = new User();
            BeanUtil.copyProperties(student, user);
            return user;
        }).collect(Collectors.toList()), key);

        //把student列表加入缓存，方便其他调用
        getAllStudent();
        return res;
    }

    @Override
    public List<Student> getAllStudent() {
        String key = RedisConstant.ALL_STUDENT_LIST;
        if (redisTemplate.hasKey(key)) {
            return RedisUtils.selectList(studentRedisTemplate, key);
        }
        List<String> studentIdList = studentCourseMapper.selectList(null).stream()
                .map(StudentCourse::getStudentId).distinct().collect(Collectors.toList());
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Student::getId, studentIdList);
        List<Student> studentList = studentMapper.selectList(wrapper);
        RedisUtils.insertList(studentRedisTemplate, studentList, key);
        return studentList;
    }

    @Override
    @Transactional
    public void addStudent(User user) {
        if (null == user) {
            log.error("user不能为空");
            return;
        }
        if (null == user.getGrade()) {
            log.error("user的grade不能为空");
            return;
        }
        String userProfession = user.getProfession();
        if (StringUtils.isEmpty(userProfession)) {
            log.error("专业不能为空");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.STUDENT_LIST);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_SCORE);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_EXPORT);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_STUDENT);

        LambdaQueryWrapper<Profession> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Profession::getName, userProfession);
        Profession profession = professionMapper.selectOne(queryWrapper);
        //专业不存在，插入专业信息
        if (null == profession) {
            profession = new Profession();
            profession.setName(userProfession);
            professionMapper.insert(profession);
        }
        Integer professionId = profession.getId();
        String professionStr = "";
        if (professionId < 10) {
            professionStr = "0" + professionId;
        } else {
            professionStr = Integer.toString(professionId);
        }
        int cnt = studentMapper.selectCount(new LambdaQueryWrapper<Student>()
                .eq(Student::getProfession, user.getProfession())
                .eq(Student::getGrade, user.getGrade())).intValue() + 1;
        String cntStr = "";
        if (cnt < 10) {
            cntStr = "0" + cnt;
        } else {
            cntStr = Integer.toString(cnt);
        }
        String no = RandomUtil.randomNumbers(1) + user.getAdmissionTime().substring(user.getAdmissionTime().length() - 2)
                + RandomUtil.randomNumbers(2) + professionStr + user.getGrade().toString().substring(user.getGrade().toString().length() - 1) + cntStr;
        Student student = new Student();
        BeanUtil.copyProperties(user, student);
        student.setId(no);
        student.setUsername(no);
        student.setPassword("123456");
        log.debug("user-->{}", user);
        log.debug("student-->{}", student);
        studentMapper.insert(student);
    }

    @Override
    public void update(User user) {
        if (null == user) {
            log.error("user为空，更新失败");
            return;
        }
        Student student = new Student();
        BeanUtil.copyProperties(user, student);
        if (null == student.getId()) {
            log.error("user的Id为空，更新失败");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.STUDENT_LIST);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_SCORE);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_EXPORT);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_STUDENT);

        studentMapper.updateById(student);
    }
}




