package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName timetable
 */
@TableName(value = "timetable")
@Data
public class Timetable implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * week id
     */
    @TableField(value = "week_id")
    private Integer weekId;

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
     * 学年
     */
    @TableField(value = "year")
    private Integer year;

    /**
     * 学期
     */
    @TableField(value = "term")
    private Integer term;

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