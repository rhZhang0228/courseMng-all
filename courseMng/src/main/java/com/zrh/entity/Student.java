package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName student
 */
@TableName(value = "student")
@Data
public class Student implements Serializable {
    /**
     *
     */
    @TableId(value = "id")
    private String id;

    /**
     *
     */
    @TableField(value = "username")
    private String username;

    /**
     *
     */
    @TableField(value = "password")
    private String password;

    /**
     *
     */
    @TableField(value = "real_name")
    private String realName;

    /**
     *
     */
    @TableField(value = "level")
    private Integer level;

    /**
     *
     */
    @TableField(value = "school")
    private String school;

    /**
     *
     */
    @TableField(value = "admission_time")
    private String admissionTime;

    /**
     *
     */
    @TableField(value = "email")
    private String email;

    /**
     *
     */
    @TableField(value = "profession")
    private String profession;

    /**
     *
     */
    @TableField(value = "grade")
    private Integer grade;

    /**
     *
     */
    @TableField(value = "phone")
    private String phone;

    /**
     *
     */
    @TableField(value = "sex")
    private Integer sex;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

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