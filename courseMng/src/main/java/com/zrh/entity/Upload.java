package com.zrh.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName upload
 */
@TableName(value = "upload")
@Data
public class Upload implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户 id
     */
    @TableField(value = "user_id")
    private String userId;

    /**
     * 用户等级
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 储存地址
     */
    @TableField(value = "url")
    private String url;

    /**
     *
     */
    @TableField(value = "is_del")
    @JsonIgnore
    private Integer isDel;

    /**
     *
     */
    @TableField(value = "object_name")
    private String objectName;

    /**
     * 乐观锁版本号
     */
    @TableField(value = "version")
    @Version
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}