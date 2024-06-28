package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName profession
 * Description 专业实体类
 */
@TableName(value = "profession")
@Data
public class Profession implements Serializable {
    /**
     * 专业id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 专业名
     */
    @TableField(value = "name")
    private String name;

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