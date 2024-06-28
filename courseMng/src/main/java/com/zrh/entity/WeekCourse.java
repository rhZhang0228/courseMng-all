package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName week
 */
@TableName(value = "week")
@Data
public class WeekCourse implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 周一
     */
    @TableField(value = "monday")
    private String monday;

    /**
     * 周二
     */
    @TableField(value = "tuesday")
    private String tuesday;

    /**
     * 周三
     */
    @TableField(value = "wednesday")
    private String wednesday;

    /**
     * 周四
     */
    @TableField(value = "thursday")
    private String thursday;

    /**
     * 周五
     */
    @TableField(value = "friday")
    private String friday;

    /**
     * 周六
     */
    @TableField(value = "saturday")
    private String saturday;

    /**
     * 周日
     */
    @TableField(value = "sunday")
    private String sunday;

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

    /**
     * 专业
     */
    @TableField(exist = false)
    private String profession;
    /**
     * 班级
     */
    @TableField(exist = false)
    private String grade;
    /**
     * 学年
     */
    @TableField(exist = false)
    private Integer year;

    /**
     * 学期
     */
    @TableField(exist = false)
    private Integer term;
}