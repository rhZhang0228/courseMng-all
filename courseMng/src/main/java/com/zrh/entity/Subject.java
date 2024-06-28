package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题
 *
 * @TableName subject
 */
@TableName(value = "subject")
@Data
public class Subject implements Serializable {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 题型
     */
    @TableField(value = "type")
    private String type;

    /**
     * 问题
     */
    @TableField(value = "question")
    private String question;

    /**
     * 答案
     */
    @TableField(value = "answer")
    private String answer;

    /**
     *
     */
    @TableField(value = "is_del")
    @JsonIgnore
    private Integer isDel;

    /**
     *
     */
    @TableField(value = "create_time")
    @JsonIgnore
    private Date createTime;

    /**
     * 乐观锁版本号
     */
    @TableField(value = "version")
    @Version
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}