package com.zrh;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.extra.mail.MailUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zrh.dto.ScoreLevelNumDto;
import com.zrh.dto.TimetableQueryParam;
import com.zrh.dto.User;
import com.zrh.entity.*;
import com.zrh.mapper.AdminMapper;
import com.zrh.mapper.CourseInfoMapper;
import com.zrh.mapper.SilentMapper;
import com.zrh.service.*;
import com.zrh.utils.MinioUtils;
import com.zrh.vo.Tree;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class CourseMngApplicationTests {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;
    @Autowired
    ScoreService scoreService;
    @Autowired
    private CourseInfoMapper courseInfoMapper;
    @Autowired
    private SilentMapper silentMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    UserService userService;

    @Autowired
    TeacherService teacherService;
    @Autowired
    AdminService adminService;
    @Autowired
    StudentService studentService;
    @Autowired
    private TimetableService timetableService;

    @Autowired
    private CourseService courseService;
    @Autowired
    private TeacherCourseService teacherCourseService;
    @Autowired
    private MinioUtils utils;

    @Test
    void minio() throws Exception {
        System.out.println(utils.getPresignedObjectUrl("2/3168901104/98d6ec65-bb2c-4b41-8f7f-ac723085b59b.jpg"));
    }

    @Test
    void sendTranscript() {
        adminService.sendTranscript();
    }

    @Test
    void resdiTemplate() {
        redisTemplate.opsForValue().set("123",
                studentService.getStudentList("3168", "环境工程", "1601", 10, 10));
    }

    @Test
    void insertTeacherCourse() {
        TeacherCourse teacherCourse = new TeacherCourse();
        teacherCourse.setCourseId("2016031002");
        teacherCourse.setTeacherId("3890281");
        teacherCourse.setUsername("3890290");
        teacherCourse.setProfession("计算机");
        teacherCourse.setGrade("2017");
        teacherCourse.setTerm(1);
        teacherCourseService.add(Arrays.asList(teacherCourse));
    }

    @Test
    void addCourse() {
        Course course = new Course();
        course.setGrade("2020");
        course.setCredits(new BigDecimal(2.0));
        course.setStart(2);
        course.setEnd(23);
        course.setProfession("test");
        course.setName("testClass");
        course.setScore(100);
        course.setNumber(16);
        course.setYear(2020);
        course.setTerm(1);
        course.setRoom("428");
        courseService.addCourse(course);
    }

    @Test
    void updateCodrse() {
        Course course = new Course();
        course.setId("1");
        course.setProfession("ttt");
        courseService.update(course);
    }

    @Test
    void testDeleteIdsCourse() {
        courseService.delete(Arrays.asList("1", "2"));
    }

    @Test
    void getTimetableByTeacher() {
        TimetableQueryParam timetableQueryParam = new TimetableQueryParam();
        timetableQueryParam.setTeacherId("3890290");
        timetableQueryParam.setWeek(1);
        List<WeekCourse> res = timetableService.getTimetableByTeacher(timetableQueryParam);
        for (WeekCourse w : res) {
            System.out.println(w);
        }
    }

    @Test
    void addStu() {
        Student student = new Student();
        student.setAdmissionTime("2024");
        student.setEmail("180@qq.com");
        student.setPhone("124323442");
        student.setProfession("计算机");
        student.setRealName("放哪手动阀");
        student.setSchool("重庆邮电大学");
        student.setSex(0);
        student.setGrade(2017);
        User user = new User();
        BeanUtil.copyProperties(student, user);
        studentService.addStudent(user);
    }

    @Test
    void studentList() {
        studentService.getStudentList("3168", "环境工程", "1601", 10, 10);
    }

    @Test
    void adminList() {
        teacherService.page(new Page<>(4, 9));
    }

    @Test
    void testCIM() {
        courseInfoMapper.selectList(null).forEach(a -> System.out.println(a));
    }

    public static void main(String[] args) {
        MailUtil.send("1809674320@qq.com", "测试", "邮件来自Hutool测试", false);

    }

    @Test
    void contextLoads() {
        System.out.println(silentMapper.selectOne(null));
    }

    @Test
    void admin() {
        System.out.println(adminMapper.selectOne(null));
    }

    @Test
    void score() {
        ScoreLevelNumDto dto = new ScoreLevelNumDto();
        dto.setLevel("0");
        dto.setUsername("root");
        dto.setProfession("电子信息工程");
        dto.setGrade("1601");
        dto.setCourseName("大学物理");
        System.out.println(scoreService.getScoreLevelNum(dto));
    }

    @Test
    void getTree() {
        List<Tree> tree = userService.getTree();
        System.out.println(tree);
        System.out.println(JSON.toJSON(tree));
    }

    @Test
    void getTeancherList() {
        System.out.println(teacherService.getTeacherList("", "", "", 5, 5));
    }

    @Test
    void updateAdmin() {
        Admin admin = adminMapper.selectById(25);
        admin.setRealName("tttttttt");
        adminMapper.updateById(admin);
    }
}
