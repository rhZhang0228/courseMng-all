package com.zrh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.dto.TimetableQueryParam;
import com.zrh.entity.*;
import com.zrh.mapper.*;
import com.zrh.service.TimetableService;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rhZhang
 * @description 针对表【timetable】的数据库操作Service实现
 * @createDate 2024-03-30 17:16:19
 */
@Service
public class TimetableServiceImpl extends ServiceImpl<TimetableMapper, Timetable>
        implements TimetableService {
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TimetableMapper timetableMapper;

    @Autowired
    private WeekCourseMapper weekCourseMapper;

    @Autowired
    private CourseInfoMapper courseInfoMapper;

    @Autowired
    private TeacherCourseMapper teacherCourseMapper;
    @Autowired
    private RedisTemplate<String, Course> courseRedisTemplate;
    @Autowired
    private RedisTemplate<String, WeekCourse> weekCourseRedisTemplate;

    @Override
    public List<WeekCourse> getTimetableByStudent(TimetableQueryParam timetableQueryParam) {
        String studentName = timetableQueryParam.getStudentName();
        if (StringUtils.isEmpty(studentName)) {
            throw new RuntimeException("学生名字不能为空");
        }
        Integer year = timetableQueryParam.getYear();
        Integer term = timetableQueryParam.getTerm();
        Integer week = timetableQueryParam.getWeek();
        StringBuilder keySb = new StringBuilder(RedisConstant.WEEK_COURSE_LIST_STUDENT);
        keySb.append("::").append(studentName);
        if (null != year) {
            keySb.append("::").append(year);
        }
        if (null != term) {
            keySb.append("::").append(term);
        }
        if (null != week) {
            keySb.append("::").append(week);
        }
        String key = keySb.toString();
        if (weekCourseRedisTemplate.hasKey(key)) {
            return RedisUtils.selectList(weekCourseRedisTemplate, key);
        }
        Student student = studentMapper.selectById(studentName);
        String profession = student.getProfession();
        Integer grade = student.getGrade();
        List<WeekCourse> weekCourseList = weekCourseMapper.getWeekCourse(profession, grade, year, term);
        handleCurrentWeekCourse(weekCourseList, profession, week);
        RedisUtils.insertList(weekCourseRedisTemplate, weekCourseList, key);
        return weekCourseList;
    }

    @Override
    public List<WeekCourse> getTimetableByTeacher(TimetableQueryParam timetableQueryParam) {
        String teacherId = timetableQueryParam.getTeacherId();
        if (StringUtils.isEmpty(teacherId)) {
            throw new RuntimeException("参数不全");
        }
        StringBuilder keySb = new StringBuilder(RedisConstant.WEEK_COURSE_LIST_TEACHER);
        keySb.append("::").append(teacherId);
        String key = keySb.toString();
        if (weekCourseRedisTemplate.hasKey(key)) {
            return RedisUtils.selectList(weekCourseRedisTemplate, key);
        }
        LambdaQueryWrapper<TeacherCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherCourse::getTeacherId, teacherId);
        //当前这个老师的任课信息
        List<TeacherCourse> teacherCourseList = teacherCourseMapper.selectList(wrapper);
        List<WeekCourse> res = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            WeekCourse weekCourse = new WeekCourse();
            dealWeek(weekCourse);
            res.add(weekCourse);
        }
        for (TeacherCourse teacherCourse : teacherCourseList) {
            //当前这个老师的任课信息的这一条信息，包括教的那个专业的哪门课
            String profession = teacherCourse.getProfession();
            String grade = teacherCourse.getGrade();
            Integer term = teacherCourse.getTerm();
            Integer week = timetableQueryParam.getWeek();
            List<WeekCourse> weekCourseList = weekCourseMapper.getWeekCourse(profession, Integer.valueOf(grade), null, term);
            handleCurrentWeekCourse(weekCourseList, profession, week);
            for (int i = 0; i < res.size(); i++) {
                handleCurrentTeacherWeekCourse(res.get(i), weekCourseList.get(i), teacherCourse.getName());
            }
        }
        RedisUtils.insertList(weekCourseRedisTemplate, res, key);
        return res;
    }

    @Override
    public void updateCourseInfo(CourseInfo courseInfo) {
        String courseId = courseInfo.getCourseId();
        if (StringUtils.isEmpty(courseId)) {
            log.error("courseId不能为空");
            return;
        }
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_MAP);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_STUDENT);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_TEACHER);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_ADMIN);

        LambdaUpdateWrapper<CourseInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(CourseInfo::getCourseId, courseId);
        courseInfoMapper.update(courseInfo, wrapper);
    }

    @Override
    public List<WeekCourse> getTimetable(TimetableQueryParam timetableQueryParam) {
        Integer year = timetableQueryParam.getYear();
        Integer term = timetableQueryParam.getTerm();
        Integer week = timetableQueryParam.getWeek();
        String profession = timetableQueryParam.getProfession();
        StringBuilder keySb = new StringBuilder(RedisConstant.WEEK_COURSE_LIST_ADMIN);
        if (StringUtils.isNotEmpty(profession)) {
            keySb.append("::").append(profession);
        }
        if (null != year) {
            keySb.append("::").append(year);
        }
        if (null != term) {
            keySb.append("::").append(term);
        }
        if (null != week) {
            keySb.append("::").append(week);
        }
        String key = keySb.toString();
        if (weekCourseRedisTemplate.hasKey(key)) {
            return RedisUtils.selectList(weekCourseRedisTemplate, key);
        }
        LambdaQueryWrapper<Timetable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(timetableQueryParam.getProfession()), Timetable::getProfession, timetableQueryParam.getProfession())
                .eq(StringUtils.isNotEmpty(timetableQueryParam.getGrade()), Timetable::getGrade, timetableQueryParam.getGrade())
                .eq(null != timetableQueryParam.getYear(), Timetable::getYear, timetableQueryParam.getYear())
                .eq(null != timetableQueryParam.getTerm(), Timetable::getTerm, timetableQueryParam.getTerm());
        Integer count = timetableMapper.selectCount(queryWrapper).intValue();
        List<WeekCourse> res = new ArrayList<>();
        if (count == 0) {
            for (int i = 0; i < 10; i++) {
                res.add(new WeekCourse());
            }
        } else {
            res = weekCourseMapper.getWeekCourse(timetableQueryParam.getProfession(), Integer.valueOf(timetableQueryParam.getGrade()), timetableQueryParam.getYear(), timetableQueryParam.getTerm());
            handleCurrentWeekCourse(res, timetableQueryParam.getProfession(), timetableQueryParam.getWeek());
        }
        RedisUtils.insertList(weekCourseRedisTemplate, res, key);
        return res;
    }

    @Override
    @Transactional
    public void add(List<WeekCourse> weekCourseList) {
        if (CollectionUtils.isEmpty(weekCourseList)) {
            log.error("weekCourseList不能为空");
            return;
        }
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_STUDENT);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_TEACHER);
        RedisUtils.deleteAll(weekCourseRedisTemplate, RedisConstant.WEEK_COURSE_LIST_ADMIN);

        String profession = weekCourseList.get(0).getProfession();
        String grade = weekCourseList.get(0).getGrade();
        Integer year = weekCourseList.get(0).getYear();
        Integer term = weekCourseList.get(0).getTerm();
        LambdaQueryWrapper<Timetable> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(profession), Timetable::getProfession, profession)
                .eq(StringUtils.isNotEmpty(grade), Timetable::getGrade, grade)
                .eq(null != year, Timetable::getYear, year)
                .eq(null != term, Timetable::getTerm, term);
        List<Timetable> timetableList = timetableMapper.selectList(wrapper);
        List<Integer> ids = timetableList.stream().map(timetable -> timetable.getId()).distinct().sorted().collect(Collectors.toList());
        List<Integer> weekIds = timetableList.stream().map(timetable -> timetable.getWeekId()).distinct().sorted().collect(Collectors.toList());
        if (ids.size() > 0) {
            //修改
            for (int i = 0; i < weekCourseList.size(); i++) {
                WeekCourse weekCourse = weekCourseList.get(i);
                weekCourse.setId(weekIds.get(i));
                dealWeek(weekCourse);
                weekCourseMapper.updateById(weekCourse);
            }
        } else {
            for (WeekCourse weekCourse : weekCourseList) {
                dealWeek(weekCourse);
                weekCourseMapper.insert(weekCourse);
                Timetable timetable = new Timetable();
                timetable.setWeekId(weekCourse.getId());
                timetable.setProfession(weekCourse.getProfession());
                timetable.setGrade(weekCourse.getGrade());
                timetable.setYear(weekCourse.getYear());
                timetable.setTerm(weekCourse.getTerm());
                timetableMapper.insert(timetable);
            }
        }
    }

    /**
     * 如果查询出来的weekCourse是当前遍历到的老师负责的课程，那么就更新res
     *
     * @param resWeekCourse
     * @param weekCourse
     * @param courseName
     */
    private void handleCurrentTeacherWeekCourse(WeekCourse resWeekCourse, WeekCourse weekCourse, String courseName) {
        if (courseName.equals(weekCourse.getMonday())) {
            resWeekCourse.setMonday(courseName);
        }

        if (courseName.equals(weekCourse.getTuesday())) {
            resWeekCourse.setTuesday(courseName);
        }

        if (courseName.equals(weekCourse.getWednesday())) {
            resWeekCourse.setWednesday(courseName);
        }

        if (courseName.equals(weekCourse.getThursday())) {
            resWeekCourse.setThursday(courseName);
        }

        if (courseName.equals(weekCourse.getFriday())) {
            resWeekCourse.setFriday(courseName);
        }

        if (courseName.equals(weekCourse.getSaturday())) {
            resWeekCourse.setSaturday(courseName);
        }

        if (courseName.equals(weekCourse.getSunday())) {
            resWeekCourse.setSunday(courseName);
        }
    }

    /**
     * 将新课程的每一项初始化为"一"
     *
     * @param weekCourse
     */
    private void dealWeek(WeekCourse weekCourse) {
        if (StringUtils.isEmpty(weekCourse.getMonday())) {
            weekCourse.setMonday("一");
        }
        if (StringUtils.isEmpty(weekCourse.getTuesday())) {
            weekCourse.setTuesday("一");
        }
        if (StringUtils.isEmpty(weekCourse.getWednesday())) {
            weekCourse.setWednesday("一");
        }
        if (StringUtils.isEmpty(weekCourse.getThursday())) {
            weekCourse.setThursday("一");
        }
        if (StringUtils.isEmpty(weekCourse.getFriday())) {
            weekCourse.setFriday("一");
        }
        if (StringUtils.isEmpty(weekCourse.getSaturday())) {
            weekCourse.setSaturday("一");
        }
        if (StringUtils.isEmpty(weekCourse.getSunday())) {
            weekCourse.setSunday("一");
        }
    }

    /**
     * 根据当前周过滤课程表中不属于当前周的课程
     *
     * @param weekCourseList
     * @param profession
     * @param week
     */
    private void handleCurrentWeekCourse(List<WeekCourse> weekCourseList, String profession, Integer week) {
        for (WeekCourse weekCourse : weekCourseList) {
            String courseName = weekCourse.getMonday();
            courseName = dealCourseName(profession, week, courseName);
            weekCourse.setMonday(courseName);

            courseName = weekCourse.getTuesday();
            courseName = dealCourseName(profession, week, courseName);
            weekCourse.setTuesday(courseName);

            courseName = weekCourse.getWednesday();
            courseName = dealCourseName(profession, week, courseName);
            weekCourse.setWednesday(courseName);

            courseName = weekCourse.getThursday();
            courseName = dealCourseName(profession, week, courseName);
            weekCourse.setThursday(courseName);

            courseName = weekCourse.getFriday();
            courseName = dealCourseName(profession, week, courseName);
            weekCourse.setFriday(courseName);

            courseName = weekCourse.getSaturday();
            courseName = dealCourseName(profession, week, courseName);
            weekCourse.setSaturday(courseName);

            courseName = weekCourse.getSunday();
            courseName = dealCourseName(profession, week, courseName);
            weekCourse.setSunday(courseName);
        }
    }

    /**
     * 处理本周是否有这个课程，没有就返回“一”
     *
     * @param profession
     * @param week
     * @param courseName
     * @return
     */
    private String dealCourseName(String profession, Integer week, String courseName) {
        CourseInfo courseInfo = courseInfoMapper.getCourseInfo(profession, courseName);
        if (null == courseInfo) {
            return courseName;
        }
        Integer start = courseInfo.getStart();
        Integer end = courseInfo.getEnd();
        if (week < start || week > end) {
            return "一";
        }
        return courseName;
    }
}




