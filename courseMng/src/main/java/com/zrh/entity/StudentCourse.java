package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName student_course
 */
@TableName(value = "student_course")
@Data
public class StudentCourse implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     *
     */
    @TableField(value = "student_id")
    private String studentId;

    /**
     *
     */
    @TableField(value = "course_id")
    private String courseId;

    /**
     *
     */
    @TableField(value = "name")
    private String name;

    /**
     *
     */
    @TableField(value = "score")
    private String score;

    /**
     *
     */
    @TableField(value = "point")
    private BigDecimal point;

    /**
     *
     */
    @TableField(value = "credits")
    private BigDecimal credits;

    /**
     *
     */
    @TableField(value = "term")
    private Integer term;

    /**
     *
     */
    @TableField(value = "year")
    private Integer year;

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