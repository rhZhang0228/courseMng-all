package com.zrh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.dto.CourseParam;
import com.zrh.dto.TeacherCourseInfoParam;
import com.zrh.entity.Course;
import com.zrh.entity.Profession;
import com.zrh.entity.Student;
import com.zrh.entity.TeacherCourse;
import com.zrh.mapper.CourseMapper;
import com.zrh.mapper.ProfessionMapper;
import com.zrh.mapper.StudentMapper;
import com.zrh.mapper.TeacherCourseMapper;
import com.zrh.service.TeacherCourseService;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import com.zrh.vo.AdminProfessionInfoVo;
import com.zrh.vo.ProfessionInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhangronghao
 * @description 针对表【teacher_course】的数据库操作Service实现
 * @createDate 2024-03-25 14:10:52
 */
@Service
public class TeacherCourseServiceImpl extends ServiceImpl<TeacherCourseMapper, TeacherCourse>
        implements TeacherCourseService {
    @Autowired
    private TeacherCourseMapper teacherCourseMapper;
    @Autowired
    private ProfessionMapper professionMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private RedisTemplate<String, ProfessionInfoVo> professionInfoVoRT;
    @Autowired
    private RedisTemplate<String, AdminProfessionInfoVo> adminProfessionInfoVoRT;
    @Autowired
    private RedisTemplate<String, TeacherCourse> teacherCourseRT;
    @Autowired
    private RedisTemplate<String, Course> courseRedisTemplate;

    @Override
    public List<ProfessionInfoVo> getProfessionInfo(String teacherId) {
        if (StringUtils.isEmpty(teacherId)) {
            log.error("teacherId不能为空");
            return new ArrayList<>();
        }
        StringBuilder keySb = new StringBuilder(RedisConstant.PROFESSION_INFO_LIST);
        keySb.append("::").append("teacher").append("::").append(teacherId);
        String key = keySb.toString();
        if (professionInfoVoRT.hasKey(key)) {
            return RedisUtils.selectList(professionInfoVoRT, key);
        }
        LambdaQueryWrapper<TeacherCourse> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeacherCourse::getTeacherId, teacherId);
        //当前老师所有的任课信息
        List<TeacherCourse> teacherCourseList = teacherCourseMapper.selectList(queryWrapper);
        Set<String> professionSet = new HashSet<>();
        teacherCourseList.forEach(teacherCourse -> professionSet.add(teacherCourse.getProfession()));
        List<ProfessionInfoVo> res = new ArrayList<>();
        for (String profession : professionSet) {
            //这个老师任课的这个专业的信息
            ProfessionInfoVo professionInfoVo = new ProfessionInfoVo();
            professionInfoVo.setTeacherId(teacherId);
            professionInfoVo.setProfession(profession);
            List<TeacherCourse> list = teacherCourseList.stream()
                    .filter(teacherCourse -> profession.equals(teacherCourse.getProfession()))
                    .collect(Collectors.toList());
            Set<String> gradeSet = new HashSet<>();
            Set<String> courseSet = new HashSet<>();
            for (TeacherCourse teacherCourse : list) {
                gradeSet.add(teacherCourse.getGrade());
                courseSet.add(teacherCourse.getName());
            }
            professionInfoVo.setGrade(new ArrayList<>(gradeSet));
            professionInfoVo.setCourse(new ArrayList<>(courseSet));
            res.add(professionInfoVo);
        }
        RedisUtils.insertList(professionInfoVoRT, res, key);
        return res;
    }

    @Override
    public List<AdminProfessionInfoVo> getProfessionInfoByAdmin() {
        StringBuilder keySb = new StringBuilder(RedisConstant.PROFESSION_INFO_LIST);
        keySb.append("::").append("admin");
        String key = keySb.toString();
        if (adminProfessionInfoVoRT.hasKey(key)) {
            return RedisUtils.selectList(adminProfessionInfoVoRT, key);
        }
        List<AdminProfessionInfoVo> res = new ArrayList<>();
        List<Profession> professionList = professionMapper.selectList(null);
        for (Profession profession : professionList) {
            AdminProfessionInfoVo professionInfoVo = new AdminProfessionInfoVo();
            professionInfoVo.setProfession(profession.getName());
            List<Student> studentList = studentMapper.selectList(new LambdaQueryWrapper<Student>().eq(Student::getProfession, profession.getName()));
            List<Integer> gradeList = studentList.stream().map(Student::getGrade).distinct().sorted().collect(Collectors.toList());
            CourseParam courseParam = new CourseParam();
            courseParam.setProfession(profession.getName());
            List<Course> courseList = courseMapper.getCourseList(courseParam);
            List<String> courseNameList = courseList.stream().map(Course::getName).distinct().collect(Collectors.toList());
            professionInfoVo.setGrade(gradeList);
            professionInfoVo.setCourse(courseNameList);
            res.add(professionInfoVo);
        }
        RedisUtils.insertList(adminProfessionInfoVoRT, res, key);
        return res;
    }

    @Override
    public TeacherCourse getCourseInfo(TeacherCourseInfoParam teacherCourseInfoParam) {
        return teacherCourseMapper.getCourseInfo(teacherCourseInfoParam);
    }

    @Override
    public List<TeacherCourse> getCourseListById(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("id不能为空");
        }
        StringBuilder keySb = new StringBuilder(RedisConstant.TEACHER_COURSE_LIST);
        keySb.append("::").append(id);
        String key = keySb.toString();
        if (teacherCourseRT.hasKey(key)) {
            return RedisUtils.selectList(teacherCourseRT, key);
        }
        LambdaQueryWrapper<TeacherCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherCourse::getTeacherId, id);
        List<TeacherCourse> teacherCourseList = teacherCourseMapper.selectList(wrapper);
        RedisUtils.insertList(teacherCourseRT, list(), key);
        return teacherCourseList;
    }

    @Override
    @Transactional
    public void add(List<TeacherCourse> teacherCourseList) {
        RedisUtils.deleteAll(teacherCourseRT, RedisConstant.TEACHER_COURSE_LIST);
        RedisUtils.deleteAll(professionInfoVoRT, RedisConstant.PROFESSION_INFO_LIST);
        RedisUtils.deleteAll(adminProfessionInfoVoRT, RedisConstant.PROFESSION_INFO_LIST);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_SCORE);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_EXPORT);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_MAP);

        List<Integer> ids = teacherCourseList.stream().filter(teacherCourse -> null != teacherCourse && null != teacherCourse.getId() && -1 != teacherCourse.getId())
                .map(teacherCourse -> teacherCourse.getId()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(ids)) {
            teacherCourseMapper.deleteBatchIds(ids);
        }
        for (TeacherCourse teacherCourse : teacherCourseList) {
            Course course = courseMapper.selectById(teacherCourse.getCourseId());
            teacherCourse.setName(course.getName());
        }
        this.saveBatch(teacherCourseList);
    }

    @Override
    public void editById(TeacherCourse teacherCourse) {
        RedisUtils.deleteAll(teacherCourseRT, RedisConstant.TEACHER_COURSE_LIST);
        RedisUtils.deleteAll(professionInfoVoRT, RedisConstant.PROFESSION_INFO_LIST);
        RedisUtils.deleteAll(adminProfessionInfoVoRT, RedisConstant.PROFESSION_INFO_LIST);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_SCORE);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_EXPORT);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_MAP);

        teacherCourseMapper.updateById(teacherCourse);
    }

    @Override
    public void deleteByIds(List<Integer> ids) {
        RedisUtils.deleteAll(teacherCourseRT, RedisConstant.TEACHER_COURSE_LIST);
        RedisUtils.deleteAll(professionInfoVoRT, RedisConstant.PROFESSION_INFO_LIST);
        RedisUtils.deleteAll(adminProfessionInfoVoRT, RedisConstant.PROFESSION_INFO_LIST);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_SCORE);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_EXPORT);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_MAP);

        teacherCourseMapper.deleteBatchIds(ids);
    }
}




