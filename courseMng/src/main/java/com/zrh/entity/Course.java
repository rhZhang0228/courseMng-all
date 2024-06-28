package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @TableName course
 */
@TableName(value = "course")
@Data
public class Course implements Serializable {
    /**
     * 编号
     */
    @TableId(value = "id")
    private String id;

    /**
     * 课程名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 学分
     */
    @TableField(value = "credits")
    private BigDecimal credits;

    /**
     * 满分
     */
    @TableField(value = "score")
    private Integer score;

    /**
     * 课时
     */
    @TableField(value = "number")
    private Integer number;

    /**
     * 届时
     */
    @TableField(value = "year")
    private Integer year;

    /**
     * 学期
     */
    @TableField(value = "term")
    private Integer term;

    /**
     * 类别 1必修 2选修
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 专业
     */
    @TableField(value = "profession")
    private String profession;

    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 班级
     */
    @TableField(exist = false)
    private String grade;

    /**
     * 姓名
     */
    @TableField(exist = false)
    private String realName;

    /**
     * 学号
     */
    @TableField(exist = false)
    private String no;

    /**
     * 学生账号
     */
    @TableField(exist = false)
    private String studentId;

    /**
     * 查询的学生绩点
     */
    @TableField(exist = false)
    private String pointByUser;

    /**
     * 查询的学生分数
     */
    @TableField(exist = false)
    private String scoreByUser;

    /**
     * 查询的学生学分
     */
    @TableField(exist = false)
    private String creditsByUser;

    /**
     * 周数 start
     */
    @TableField(exist = false)
    private Integer start;

    /**
     * 周数 end
     */
    @TableField(exist = false)
    private Integer end;

    /**
     * 教室
     */
    @TableField(exist = false)
    private String room;

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