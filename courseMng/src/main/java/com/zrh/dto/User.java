package com.zrh.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Description 登陆信息
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 确认密码
     */
    private String passwordAgain;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 学校
     */
    private String school;
    /**
     * 入学时间
     */
    private String admissionTime;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 专业
     */
    private String profession;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 班级
     */
    private Integer grade;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * token
     */
    private String token;
    /**
     * refreshToken
     */
    private String refreshToken;
    /**
     * 默哀模式
     */
    private Integer state;
}
