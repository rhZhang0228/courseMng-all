package com.zrh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.dto.CourseParam;
import com.zrh.entity.Course;
import com.zrh.entity.CourseInfo;
import com.zrh.entity.Profession;
import com.zrh.entity.WeekCourse;
import com.zrh.mapper.CourseInfoMapper;
import com.zrh.mapper.CourseMapper;
import com.zrh.mapper.ProfessionMapper;
import com.zrh.service.CourseService;
import com.zrh.utils.PagingResult;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangronghao
 * @description 针对表【course】的数据库操作Service实现
 * @createDate 2024-03-27 12:53:03
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
        implements CourseService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private ProfessionMapper professionMapper;
    @Autowired
    private RedisTemplate<String, Course> redisTemplate;
    @Autowired
    private RedisTemplate<String, WeekCourse> weekCourseRedisTemplate;

    @Override
    public PagingResult<Course> getCourseList(String code, Integer offset, Integer limit) {
        StringBuilder keySb = new StringBuilder(RedisConstant.COURSE_LIST);
        if (StringUtils.isNotEmpty(code)) {
            keySb.append("::").append(code);
        }
        String key = keySb.toString();
        if (redisTemplate.hasKey(key)) {
            return RedisUtils.selectPage(redisTemplate, key, offset, limit);
        }

        Integer currentPage = offset / limit + 1;
        Page<Course> page = new Page<>(currentPage, limit);
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(code)) {
            wrapper.like(Course::getName, code).or().like(Course::getProfession, code);
        }
        wrapper.orderByDesc(Course::getCreateTime);
        Page<Course> coursePage = courseMapper.selectPage(page, wrapper);
        RedisUtils.insertList(redisTemplate, courseMapper.selectList(wrapper), key);
        return new PagingResult<>(coursePage.getTotal(), coursePage.getRecords());
    }

    @Override
    public List<Course> getCourseList(CourseParam courseParam) {
        StringBuilder keySb = new StringBuilder(RedisConstant.COURSE_LIST_MAP);
        if (StringUtils.isNotEmpty(courseParam.getProfession())) {
            keySb.append("::").append(courseParam.getProfession());
        }
        if (StringUtils.isNotEmpty(courseParam.getCourseName())) {
            keySb.append("::").append(courseParam.getCourseName());
        }
        if (StringUtils.isNotEmpty(courseParam.getGrade())) {
            keySb.append("::").append(courseParam.getGrade());

        }
        if (null != courseParam.getTerm()) {
            keySb.append("::").append(courseParam.getTerm());
        }
        String key = keySb.toString();
        if (redisTemplate.hasKey(key)) {
            return RedisUtils.selectList(redisTemplate, key);
        }
        List<Course> courseList = courseMapper.getCourseList(courseParam);
        RedisUtils.insertList(redisTemplate, courseList, key);;
        return courseList;
    }

    @Override
    @Transactional
    public void delete(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            log.error("idx为空");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.COURSE_LIST);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_STUDENT);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_TEACHER);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_ADMIN);

        courseMapper.deleteBatchIds(ids);
        LambdaQueryWrapper<CourseInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CourseInfo::getCourseId, ids);
        courseInfoMapper.delete(wrapper);
    }

    @Transactional
    @Override
    public void update(Course course) {
        String id = course.getId();
        if (StringUtils.isEmpty(id)) {
            log.error("id不能为空");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.COURSE_LIST);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_STUDENT);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_TEACHER);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_ADMIN);

        courseMapper.updateById(course);
        CourseInfo courseInfo = new CourseInfo();
        courseInfo.setProfession(course.getProfession());
        courseInfo.setCourseId(course.getId());
        LambdaUpdateWrapper<CourseInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CourseInfo::getCourseId, id);
        courseInfoMapper.update(courseInfo, wrapper);
    }

    @Override
    @Transactional
    public void addCourse(Course course) {
        String professionName = course.getProfession();
        if (StringUtils.isEmpty(professionName)) {
            log.error("专业名字不能为空");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.COURSE_LIST);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_STUDENT);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_TEACHER);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_ADMIN);

        LambdaQueryWrapper<Profession> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Profession::getName, professionName);
        Profession profession = professionMapper.selectOne(wrapper);
        String preffix = "";
        if (profession.getId() < 10) {
            preffix = course.getYear().toString() + "0" + profession.getId() + course.getTerm();
        } else {
            preffix = course.getYear().toString() + profession.getId() + course.getTerm();
        }
        LambdaQueryWrapper<Course> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper.eq(Course::getProfession, course.getProfession()).eq(Course::getYear, course.getYear())
                .orderByDesc(Course::getId).last("limit 1");
        Course maxCourse = courseMapper.selectOne(courseWrapper);
        String courseId = "";
        if (null != maxCourse) {
            String maxId = maxCourse.getId();
            int num = Integer.parseInt(maxId.substring(7)) + 1;
            if (num < 10) {
                courseId = "00" + num;
            } else if (num < 100) {
                courseId = "0" + num;
            } else {
                courseId = Integer.toString(num);
            }
        } else {
            courseId = "001";
        }
        courseId = preffix + courseId;
        course.setId(courseId);
        courseMapper.insert(course);
        LambdaQueryWrapper<CourseInfo> deleteWarapper = new LambdaQueryWrapper<>();
        deleteWarapper.eq(CourseInfo::getCourseId, courseId);
        courseInfoMapper.delete(deleteWarapper);
        CourseInfo courseInfo = new CourseInfo();
        if (null == course.getStart()) {
            courseInfo.setStart(1);
        } else {
            courseInfo.setStart(course.getStart());
        }
        if (null == course.getEnd()) {
            courseInfo.setEnd(25);
        } else {
            courseInfo.setEnd(course.getEnd());
        }
        if (StringUtils.isNotEmpty(course.getRoom())) {
            courseInfo.setRoom(course.getRoom());
        } else {
            courseInfo.setRoom("暂未分配");
        }
        courseInfo.setCourseId(courseId);
        courseInfo.setProfession(course.getProfession());
        courseInfoMapper.insert(courseInfo);
    }
}




