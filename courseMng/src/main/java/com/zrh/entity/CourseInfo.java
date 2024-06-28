package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName course_info
 */
@TableName(value = "course_info")
@Data
public class CourseInfo implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private String courseId;

    /**
     * 周数 start
     */
    @TableField(value = "start")
    private Integer start;

    /**
     * 周数 end
     */
    @TableField(value = "end")
    private Integer end;

    /**
     * 教室
     */
    @TableField(value = "room")
    private String room;

    /**
     * 专业
     */
    @TableField(value = "profession")
    private String profession;

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