package com.zrh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zrh.dto.CourseListQueryParam;
import com.zrh.dto.Score;
import com.zrh.dto.ScoreLevelNumDto;
import com.zrh.dto.UserTotalQueryParam;
import com.zrh.entity.Course;
import com.zrh.entity.StudentCourse;
import com.zrh.mapper.AdminMapper;
import com.zrh.mapper.StudentCourseMapper;
import com.zrh.mapper.StudentMapper;
import com.zrh.mapper.TeacherMapper;
import com.zrh.service.ScoreService;
import com.zrh.utils.PagingResult;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import com.zrh.utils.ScoreConstant;
import com.zrh.vo.ScoreLevelNumVo;
import com.zrh.vo.UserTotalScoreVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ScoreServiceImpl implements ScoreService {
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    @Autowired
    private RedisTemplate<String, Course> courseRedisTemplate;

    @Override
    public List<ScoreLevelNumVo> getScoreLevelNum(ScoreLevelNumDto scoreLevelNumDto) {
        String level = scoreLevelNumDto.getLevel();
        if (StringUtils.isEmpty(level)) {
            throw new RuntimeException("level不能为空");
        }
        String key = this.getCourseListExportKeyByScoreLevelNumDto(scoreLevelNumDto);
        List<ScoreLevelNumVo> res = new ArrayList<>();
        List<Course> courseList = new ArrayList<>();
        if (courseRedisTemplate.hasKey(key)) {
            courseList = RedisUtils.selectList(courseRedisTemplate, key);
        } else {
            switch (level) {
                case "0":
                    courseList = adminMapper.getExportList(scoreLevelNumDto);
                    break;
                case "1":
                    courseList = teacherMapper.getExportList(scoreLevelNumDto);
                    break;
                case "2":
                    courseList = studentMapper.getExportList(scoreLevelNumDto);
                    break;
                default:
                    throw new RuntimeException("level非法");
            }
            RedisUtils.insertList(courseRedisTemplate, courseList, key);
        }
        res = dealScore(courseList);
        return res;
    }

    private String getCourseListExportKeyByScoreLevelNumDto(ScoreLevelNumDto scoreLevelNumDto) {
        String profession = scoreLevelNumDto.getProfession();
        String grade = scoreLevelNumDto.getGrade();
        String username = scoreLevelNumDto.getUsername();
        String courseName = scoreLevelNumDto.getCourseName();
        String studentName = scoreLevelNumDto.getStudentName();
        String level = scoreLevelNumDto.getLevel();
        String year = scoreLevelNumDto.getYear();
        String term = scoreLevelNumDto.getTerm();
        StringBuilder keySb = new StringBuilder(RedisConstant.COURSE_LIST_EXPORT);
        keySb.append("::").append(level);
        if (StringUtils.isNotEmpty(profession)) {
            keySb.append("::").append(profession);
        }
        if (StringUtils.isNotEmpty(grade)) {
            keySb.append("::").append(grade);
        }
        if (StringUtils.isNotEmpty(username)) {
            keySb.append("::").append(username);
        }
        if (StringUtils.isNotEmpty(courseName)) {
            keySb.append("::").append(courseName);
        }
        if (StringUtils.isNotEmpty(studentName)) {
            keySb.append("::").append(studentName);
        }
        if (StringUtils.isNotEmpty(year)) {
            keySb.append("::").append(year);
        }
        if (StringUtils.isNotEmpty(term)) {
            keySb.append("::").append(term);
        }
        String key = keySb.toString();
        return key;
    }

    @Override
    public List<Course> getExportList(ScoreLevelNumDto scoreLevelNumDto) {
        String level = scoreLevelNumDto.getLevel();
        if (StringUtils.isEmpty(level)) {
            throw new RuntimeException("level不能为空");
        }
        String key = this.getCourseListExportKeyByScoreLevelNumDto(scoreLevelNumDto);
        if (courseRedisTemplate.hasKey(key)) {
            return RedisUtils.selectList(courseRedisTemplate, key);
        }
        List<Course> courseList = new ArrayList<>();
        switch (level) {
            case "0":
                courseList = adminMapper.getExportList(scoreLevelNumDto);
                break;
            case "1":
                courseList = teacherMapper.getExportList(scoreLevelNumDto);
                break;
            case "2":
                courseList = studentMapper.getExportList(scoreLevelNumDto);
                break;
            default:
                throw new RuntimeException("level非法");
        }
        RedisUtils.insertList(courseRedisTemplate, courseList, key);
        return courseList;
    }

    @Override
    public PagingResult<Course> getCourseList(CourseListQueryParam courseListQueryParam, Integer offset, Integer limit) {
        String profession = courseListQueryParam.getProfession();
        String grade = courseListQueryParam.getGrade();
        String username = courseListQueryParam.getUsername();
        String courseName = courseListQueryParam.getCourseName();
        String studentName = courseListQueryParam.getStudentName();
        String level = courseListQueryParam.getLevel();
        Integer year = courseListQueryParam.getYear();
        Integer term = courseListQueryParam.getTerm();
        if (StringUtils.isEmpty(level)) {
            throw new RuntimeException("level不能为空");
        }
        StringBuilder keySb = new StringBuilder(RedisConstant.COURSE_LIST_SCORE);
        keySb.append("::").append(level);
        if (StringUtils.isNotEmpty(profession)) {
            keySb.append("::").append(profession);
        }
        if (StringUtils.isNotEmpty(grade)) {
            keySb.append("::").append(grade);
        }
        if (StringUtils.isNotEmpty(username)) {
            keySb.append("::").append(username);
        }
        if (StringUtils.isNotEmpty(courseName)) {
            keySb.append("::").append(courseName);
        }
        if (StringUtils.isNotEmpty(studentName)) {
            keySb.append("::").append(studentName);
        }
        if (null != year) {
            keySb.append("::").append(year);
        }
        if (null != term) {
            keySb.append("::").append(term);
        }
        String key = keySb.toString();
        if (courseRedisTemplate.hasKey(key)) {
            return RedisUtils.selectPage(courseRedisTemplate, key, offset, limit);
        }
        Integer currentPage = offset / limit + 1;
        Page<Course> page = new Page<>(currentPage, limit);
        page.setOptimizeCountSql(false);
//        page.setOptimizeJoinOfCountSql(false);
        switch (level) {
            case "0":
                page = adminMapper.getCourseList(courseListQueryParam, page);
                RedisUtils.insertList(courseRedisTemplate, adminMapper.getCourseList(courseListQueryParam), key);
                break;
            case "1":
                page = teacherMapper.getCourseList(courseListQueryParam, page);
                RedisUtils.insertList(courseRedisTemplate, teacherMapper.getCourseList(courseListQueryParam), key);
                break;
            case "2":
                page = studentMapper.getCourseList(courseListQueryParam, page);
                RedisUtils.insertList(courseRedisTemplate, studentMapper.getCourseList(courseListQueryParam), key);
                break;
            default:
                throw new RuntimeException("level非法");
        }
        return new PagingResult<>(page.getTotal(), page.getRecords());
    }

    @Override
    public UserTotalScoreVo getUserTotal(UserTotalQueryParam userTotalQueryParam) {
        String level = userTotalQueryParam.getLevel();
        if (StringUtils.isEmpty(level)) {
            throw new RuntimeException("level不能为空");
        }
        if ("2".equals(level)) {
            LambdaQueryWrapper<StudentCourse> wrapper = new LambdaQueryWrapper<>();
            String studentName = userTotalQueryParam.getStudentName();
            if (StringUtils.isEmpty(studentName)) {
                throw new RuntimeException("studentName不能为空");
            }
            wrapper.eq(StudentCourse::getStudentId, studentName);
            List<StudentCourse> studentCourseList = studentCourseMapper.selectList(wrapper);
            double totalCredits = 0.00;
            double totalPoint = 0.00;
            for (StudentCourse studentCourse : studentCourseList) {
                double credits = studentCourse.getCredits().doubleValue();
                double point = studentCourse.getPoint().doubleValue();
                totalCredits += credits;
                totalPoint += point;
            }
            UserTotalScoreVo res = new UserTotalScoreVo();
            res.setPoint(totalPoint);
            res.setCredits(totalCredits);
            return res;
        } else if ("1".equals(level)) {
            ScoreLevelNumDto scoreLevelNumDto = new ScoreLevelNumDto();
            BeanUtil.copyProperties(userTotalQueryParam, scoreLevelNumDto);
            List<Course> courseList = teacherMapper.getExportList(scoreLevelNumDto);
            return courseStatistics(courseList);
        } else if ("0".equals(level)) {
            ScoreLevelNumDto scoreLevelNumDto = new ScoreLevelNumDto();
            BeanUtil.copyProperties(userTotalQueryParam, scoreLevelNumDto);
            List<Course> courseList = adminMapper.getExportList(scoreLevelNumDto);
            return courseStatistics(courseList);
        } else {
            throw new RuntimeException("level非法");
        }
    }

    @Override
    public void addEntry(List<Score> scoreList) {
        if (CollectionUtils.isEmpty(scoreList)) {
            return;
        }
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_SCORE);
        RedisUtils.deleteAll(courseRedisTemplate, RedisConstant.COURSE_LIST_EXPORT);
        for (Score score : scoreList) {
            double scoreByUser = Double.parseDouble(score.getScoreByUser());
            Integer totalScore = score.getScore();
            double percentage = scoreByUser / totalScore;
            //满绩点为5
            BigDecimal bg = new BigDecimal(percentage * 5);
            //真实绩点保留两位小数
            double point = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            String pointStr = scoreByUser >= 60 ? String.valueOf(point) : "0";
            String credits = scoreByUser >= totalScore * 0.6 ? score.getCredits() : "0.00";
            score.setPointByUser(pointStr);
            score.setCreditsByUser(credits);

            if (StringUtils.isEmpty(score.getNo()) || StringUtils.isEmpty(score.getNo())) {
                throw new RuntimeException("参数不完整");
            }
            LambdaQueryWrapper<StudentCourse> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(StudentCourse::getStudentId, score.getNo());
            queryWrapper.eq(StudentCourse::getName, score.getName());
            StudentCourse studentCourse = studentCourseMapper.selectOne(queryWrapper);
            if (null == studentCourse) {
                studentCourse = new StudentCourse();
                studentCourse.setStudentId(score.getNo());
                studentCourse.setName(score.getName());
                studentCourse.setCredits(new BigDecimal(score.getCreditsByUser()));
                studentCourse.setPoint(new BigDecimal(score.getPointByUser()));
                studentCourse.setScore(score.getScoreByUser());
                studentCourse.setYear(score.getYear());
                studentCourse.setTerm(score.getTerm());
                studentCourse.setCourseId(score.getCourseId());
                studentCourseMapper.insert(studentCourse);
            } else {
                studentCourse.setCredits(new BigDecimal(score.getCreditsByUser()));
                studentCourse.setPoint(new BigDecimal(score.getPointByUser()));
                studentCourse.setScore(score.getScoreByUser());
                studentCourseMapper.updateById(studentCourse);
            }
        }
    }

    /**
     * 拿到课程的最大值、最小值、平均值
     *
     * @param courseList
     * @return
     */
    private UserTotalScoreVo courseStatistics(List<Course> courseList) {
        UserTotalScoreVo res = new UserTotalScoreVo();
        double min = courseList.stream().mapToDouble(course -> Double.parseDouble(course.getScoreByUser()))
                .min().getAsDouble();
        double max = courseList.stream().mapToDouble(course -> Double.parseDouble(course.getScoreByUser()))
                .max().getAsDouble();
        double average = courseList.stream().mapToDouble(course -> Double.parseDouble(course.getScoreByUser()))
                .average().getAsDouble();
        res.setMin(min);
        res.setMax(max);
        res.setAverage(average);
        return res;
    }

    public static void main(String[] args) {
        BigDecimal zero = BigDecimal.ZERO;
        System.out.println(zero);
        System.out.println(zero.doubleValue());
        System.out.println(Double.NaN);
    }

    /**
     * 处理成绩各个分布的个数
     *
     * @param courseList
     * @return
     */
    private List<ScoreLevelNumVo> dealScore(List<Course> courseList) {
        List<ScoreLevelNumVo> res = new ArrayList<>();
        if (CollectionUtils.isEmpty(courseList)) {
            return res;
        }
        Map<String, Integer> map = new HashMap<>();
        for (Course course : courseList) {
            String score = course.getScoreByUser();
            Integer fulScore = course.getScore();
            if (StringUtils.isEmpty(score)) {
                map.put(ScoreConstant.UNKNOWN_LABEL, map.getOrDefault(ScoreConstant.UNKNOWN_LABEL, 0) + 1);
                continue;
            }
            double percentageScore = Double.parseDouble(score) * 100.0 / fulScore;
            if (percentageScore < 60) {
                map.put(ScoreConstant.FAIL_LABEL, map.getOrDefault(ScoreConstant.FAIL_LABEL, 0) + 1);
            } else if (percentageScore < 85) {
                map.put(ScoreConstant.PASS_LABEL, map.getOrDefault(ScoreConstant.PASS_LABEL, 0) + 1);
            } else if (percentageScore <= 100) {
                map.put(ScoreConstant.GOOD_LABEL, map.getOrDefault(ScoreConstant.GOOD_LABEL, 0) + 1);
            } else {
                log.error("分数异常");
            }
        }
        res.add(new ScoreLevelNumVo(ScoreConstant.GOOD_LABEL, map.get(ScoreConstant.GOOD_LABEL)));
        res.add(new ScoreLevelNumVo(ScoreConstant.PASS_LABEL, map.get(ScoreConstant.PASS_LABEL)));
        res.add(new ScoreLevelNumVo(ScoreConstant.FAIL_LABEL, map.get(ScoreConstant.FAIL_LABEL)));
        if (map.containsKey(ScoreConstant.UNKNOWN_LABEL)) {
            res.add(new ScoreLevelNumVo(ScoreConstant.UNKNOWN_LABEL, map.get(ScoreConstant.UNKNOWN_LABEL)));
        }
        return res;
    }
}
