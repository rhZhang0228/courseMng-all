package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * Description 教师课程任命表实体类
 *
 * @TableName teacher_course
 */
@TableName(value = "teacher_course")
@Data
public class TeacherCourse implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 教师id
     */
    @TableField(value = "teacher_id")
    private String teacherId;

    /**
     * 教师用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 课程名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 专业
     */
    @TableField(value = "profession")
    private String profession;

    /**
     * 班级
     */
    @TableField(value = "grade")
    private String grade;

    /**
     * 学期
     */
    @TableField(value = "term")
    private Integer term;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private String courseId;

    /**
     * 节数
     */
    @TableField(exist = false)
    private Integer number;

    /**
     * 教师姓名
     */
    @TableField(exist = false)
    private String realName;

    /**
     * 学分
     */
    @TableField(exist = false)
    private String credits;

    /**
     * 类型
     */
    @TableField(exist = false)
    private Integer type;

    /**
     * 开始时间
     */
    @TableField(exist = false)
    private Integer start;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private Integer end;

    /**
     * 教室
     */
    @TableField(exist = false)
    private Integer room;

    /**
     *
     */
    @TableField(value = "is_del")
    @JsonIgnore
    private Integer isDel;

    /**
     * 乐观锁版本号
     */
    @TableField(value = "version")
    @Version
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}