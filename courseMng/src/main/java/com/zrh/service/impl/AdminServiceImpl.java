package com.zrh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zrh.dto.ScoreLevelNumDto;
import com.zrh.dto.User;
import com.zrh.entity.Admin;
import com.zrh.entity.Course;
import com.zrh.entity.Student;
import com.zrh.mapper.AdminMapper;
import com.zrh.service.AdminService;
import com.zrh.service.ScoreService;
import com.zrh.service.StudentService;
import com.zrh.task.SendTranscriptTask;
import com.zrh.utils.PagingResult;
import com.zrh.utils.RedisConstant;
import com.zrh.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author zhangronghao
 * @description 针对表【admin】的数据库操作Service实现
 * @createDate 2024-03-25 16:37:57
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
        implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RedisTemplate<String, User> redisTemplate;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ScoreService scoreService;

    @Override
    public PagingResult<User> getAdminList(String code, Integer limit, Integer offset) {
        StringBuilder keySb = new StringBuilder();
        keySb.append(RedisConstant.ADMIN_LIST);
        if (StringUtils.isNotEmpty(code)) {
            keySb.append("::").append(code);
        }
        String key = keySb.toString();
        //从缓存取数据
        if (redisTemplate.hasKey(key)) {
            PagingResult<User> result = RedisUtils.selectPage(redisTemplate, key, offset, limit);
            return result;
        }

        Integer currentPage = offset / limit + 1;
        Page<Admin> page = new Page<>(currentPage, limit);
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(code)) {
            wrapper.like(Admin::getId, code).or().like(Admin::getRealName, code);

        }
        Page<Admin> resPage = adminMapper.selectPage(page, wrapper);
        List<User> userList = resPage.getRecords().stream().map(admin -> {
            User user = new User();
            BeanUtil.copyProperties(admin, user);
            return user;
        }).collect(Collectors.toList());
        PagingResult<User> pagingResult = new PagingResult<>();
        pagingResult.setItems(userList);
        pagingResult.setTotalCount(resPage.getTotal());
        RedisUtils.insertList(redisTemplate, adminMapper.selectList(wrapper).stream().map(admin -> {
            User user = new User();
            BeanUtil.copyProperties(admin, user);
            return user;
        }).collect(Collectors.toList()), key);
        return pagingResult;
    }

    @Override
    public void add(User user) {
        RedisUtils.deleteAll(redisTemplate, RedisConstant.ADMIN_LIST);
        Admin admin = new Admin();
        BeanUtil.copyProperties(user, admin);
        Integer count = adminMapper.selectCount(null).intValue();
        Integer suffixId = count + 1;
        String prefixId = RandomUtil.randomNumbers(8 - suffixId.toString().length());
        admin.setUsername(prefixId + suffixId);
        admin.setPassword("root");
        adminMapper.insert(admin);
    }

    @Override
    public void upadte(User user) {
        if (null == user) {
            log.error("user为空，更新失败");
            return;
        }
        Admin admin = new Admin();
        BeanUtil.copyProperties(user, admin);
        if (null == admin.getId()) {
            log.error("user的Id为空，更新失败");
            return;
        }
        RedisUtils.deleteAll(redisTemplate, RedisConstant.ADMIN_LIST);
        adminMapper.updateById(admin);
    }

    @Override
    public void sendTranscript() {
        //本来是应该给所有人发送成绩单的，但是个人QQ邮箱有发送限制，所以这里只发送两个人的
//        List<Student> studentList = studentService.getAllStudent();
        List<Student> studentList = Arrays.asList(studentService.getById("3168901104"),
                studentService.getById("3168901101"));
//        LocalDate now = LocalDate.now();
        //因为数据库中的数据都是2016年的，这里直接new一个对象表示now
        LocalDate now = LocalDate.of(2016, 9, 1);
        int month = now.getMonthValue();
        int year = now.getYear();
        int queryYear;
        int queryTerm;
        if (month < 3) {
            //在上学期，寒假也算上学期，2017年2月是2016学年的第一学期
            queryYear = year - 1;
            queryTerm = 1;
        } else if (month < 9) {
            queryYear = year - 1;
            //在下学期，暑假也是下学期，2017年6月是2016学年的第二学期
            queryTerm = 2;
        } else {
            //本年的第一学期
            queryYear = year;
            queryTerm = 1;
        }
        Map<Student, List<Course>> studentListMap = studentList.stream().collect(Collectors.toMap(student -> student,
                student -> {
                    ScoreLevelNumDto scoreLevelNumDto = new ScoreLevelNumDto();
                    scoreLevelNumDto.setYear(String.valueOf(queryYear));
                    scoreLevelNumDto.setTerm(String.valueOf(queryTerm));
                    scoreLevelNumDto.setStudentName(student.getId());
                    scoreLevelNumDto.setLevel("2");
                    List<Course> courseList = scoreService.getExportList(scoreLevelNumDto);
                    return courseList;
                }
        ));
        ExecutorService pool = new ThreadPoolExecutor(4, 10, 1, TimeUnit.HOURS,
                new LinkedBlockingDeque<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        for (Map.Entry<Student, List<Course>> entry : studentListMap.entrySet()) {
            Student student = entry.getKey();
            List<Course> courseList = entry.getValue();
            pool.submit(new SendTranscriptTask(student, courseList));
            log.debug("加入了线程");
        }
        pool.shutdown();
    }

    public static void main(String[] args) {
        ExecutorService pool = new ThreadPoolExecutor(4, 10, 1, TimeUnit.HOURS,
                new LinkedBlockingDeque<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        Student student = new Student();
        student.setEmail("1809674320@qq.com");
        student.setRealName("张荣豪111");
        List<Course> list = new ArrayList<>();
        Course course = new Course();
        course.setName("计网");
        course.setScoreByUser("78");
        course.setPointByUser("2.00");
        course.setCreditsByUser("4.00");
        course.setType(1);
        course.setProfession("计算机");
        course.setGrade("2016");
        list.add(course);
        pool.submit(new SendTranscriptTask(student, list));
        pool.shutdown();
    }
}
