package com.zrh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.crypto.asymmetric.KeyType;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zrh.dto.UpdatePasswordDto;
import com.zrh.dto.User;
import com.zrh.entity.Admin;
import com.zrh.entity.Silent;
import com.zrh.entity.Student;
import com.zrh.entity.Teacher;
import com.zrh.mapper.AdminMapper;
import com.zrh.mapper.SilentMapper;
import com.zrh.mapper.StudentMapper;
import com.zrh.mapper.TeacherMapper;
import com.zrh.service.UserService;
import com.zrh.utils.RsaConstant;
import com.zrh.vo.Tree;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private SilentMapper silentMapper;

    @Override
    public User getUserByLevelAndId(String level, String id) {
        User user = new User();
        switch (level) {
            case "0":
                Admin admin = adminMapper.selectById(id);
                BeanUtil.copyProperties(admin, user);
                log.debug("admin-->{}", admin);
                log.debug("user-->{}", user);
                break;
            case "1":
                Teacher teacher = teacherMapper.selectById(id);
                BeanUtil.copyProperties(teacher, user);
                log.debug("teacher-->{}", teacher);
                log.debug("user-->{}", user);
                break;
            case "2":
                Student student = studentMapper.selectById(id);
                BeanUtil.copyProperties(student, user);
                log.debug("student-->{}", student);
                log.debug("user-->{}", user);
                break;
            default:
                log.error("level错误");
        }
        return user;
    }

    @Override
    public String getToken(User user, long time) {
        Date start = new Date();
        Date end = new Date(start.getTime() + time);
        String audience = user.getLevel().toString() + user.getId().toString();
        String token = JWT.create().withAudience(audience).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    @Override
    public String getToken(User user) {
        return getToken(user, DateUnit.HOUR.getMillis());

    }

    @Override
    public String getRefreshToken(User user) {
        return getToken(user, DateUnit.DAY.getMillis());

    }

    @Override
    public User getUserInfo(String username, String password, String level) {
        User user = new User();
        String passwordDecrypt = RsaConstant.rsa.decryptStr(password, KeyType.PrivateKey);
        switch (level) {
            case "0": {
                LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Admin::getUsername, username);
                Admin admin = adminMapper.selectOne(queryWrapper);
                if (null == admin) {
                    log.error("用户不存在");
                    throw new RuntimeException("用户不存在");
                }
                if (!passwordDecrypt.equals(admin.getPassword())) {
                    log.error("密码错误");
                    throw new RuntimeException("密码错误");
                }
                BeanUtil.copyProperties(admin, user);
                log.debug("admin-->{}", admin);
                log.debug("user-->{}", user);
                break;
            }
            case "1": {
                LambdaQueryWrapper<Teacher> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Teacher::getUsername, username);
                Teacher teacher = teacherMapper.selectOne(queryWrapper);
                if (null == teacher) {
                    log.error("用户不存在");
                    throw new RuntimeException("用户不存在");
                }
                if (!passwordDecrypt.equals(teacher.getPassword())) {
                    log.error("密码错误");
                    throw new RuntimeException("密码错误");
                }
                BeanUtil.copyProperties(teacher, user);
                log.debug("teacher-->{}", teacher);
                log.debug("user-->{}", user);
                break;
            }
            case "2": {
                LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Student::getUsername, username);
                Student student = studentMapper.selectOne(queryWrapper);
                if (null == student) {
                    log.error("用户不存在");
                    throw new RuntimeException("用户不存在");
                }
                if (!passwordDecrypt.equals(student.getPassword())) {
                    log.error("密码错误");
                    throw new RuntimeException("密码错误");
                }
                BeanUtil.copyProperties(student, user);
                log.debug("student-->{}", student);
                log.debug("user-->{}", user);
                break;
            }
            default:
                log.error("level错误");
                throw new RuntimeException("level错误");
        }
        return user;

    }

    @Override
    public boolean getSilent() {
        Silent silent = silentMapper.selectOne(null);
        return silent.getState() == 1;
    }

    @Override
    public boolean setSilent(Integer state) {
        if (null == state) {
            log.error("state为null，更改失败");
            return false;
        }
        Silent silent = silentMapper.selectOne(null);
        silent.setState(state);
        silentMapper.updateById(silent);
        return state == 1;
    }

    @Override
    public boolean updatePassword(UpdatePasswordDto updatePasswordDto) {
        String level = updatePasswordDto.getLevel();
        String password = RsaConstant.rsa.decryptStr(updatePasswordDto.getPassword(), KeyType.PrivateKey);
        String passwordAgain = RsaConstant.rsa.decryptStr(updatePasswordDto.getPasswordAgain(), KeyType.PrivateKey);
        String username = updatePasswordDto.getUsername();
        if (StringUtils.isEmpty(level) || StringUtils.isEmpty(passwordAgain)
                || StringUtils.isEmpty(password) || StringUtils.isEmpty(password)) {
            return false;
        }
        switch (level) {
            case "0": {
                LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Admin::getUsername, username);
                Admin admin = adminMapper.selectOne(wrapper);
                if (null == admin) {
                    return false;
                }
                if (!password.equals(admin.getPassword())) {
                    return false;
                }
                admin.setPassword(passwordAgain);
                int cnt = adminMapper.updateById(admin);
                return cnt > 0;

            }

            case "1": {
                LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Teacher::getUsername, username);
                Teacher teacher = teacherMapper.selectOne(wrapper);
                if (null == teacher) {
                    return false;
                }
                if (!password.equals(teacher.getPassword())) {
                    return false;
                }
                teacher.setPassword(passwordAgain);
                int cnt = teacherMapper.updateById(teacher);
                return cnt > 0;
            }
            case "2": {
                LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(Student::getUsername, username);
                Student student = studentMapper.selectOne(wrapper);
                if (null == student) {
                    return false;
                }
                if (!password.equals(student.getPassword())) {
                    return false;
                }
                student.setPassword(passwordAgain);
                int cnt = studentMapper.updateById(student);
                return cnt > 0;

            }
            default:
                log.error("错误，level非法");
                return false;
        }
    }

    @Override
    public List<Tree> getTree() {
        List<Tree> res = new ArrayList<>();
        List<String> professionList = studentMapper.selectList(null).stream()
                .map(Student::getProfession).distinct().collect(Collectors.toList());
        List<Tree> studentTreeList = new ArrayList<>();
        for (String profession : professionList) {
            Tree tree = new Tree();
            tree.setLabel(profession);
            LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Student::getProfession, profession);
            List<Integer> gradeList = studentMapper.selectList(wrapper).stream()
                    .map(Student::getGrade).distinct().sorted().collect(Collectors.toList());
            List<Tree> professionChildren = new ArrayList<>();
            for (Integer grade : gradeList) {
                professionChildren.add(new Tree(grade.toString(), new ArrayList<>()));
            }
            tree.setChildren(professionChildren);
            studentTreeList.add(tree);
            if (profession.equals("工程造价")) {
                log.debug("---->{}", tree);
            }
        }
        res.add(new Tree("学生", studentTreeList));
        res.add(new Tree("教师", new ArrayList<>()));
        res.add(new Tree("管理员", new ArrayList<>()));
        return res;
    }

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
        System.out.println(System.currentTimeMillis());
    }
}
