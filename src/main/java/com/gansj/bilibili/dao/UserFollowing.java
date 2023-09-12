package com.gansj.bilibili.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户关注表
 * @TableName user_following
 */
@TableName(value ="user_following")
@Data
public class UserFollowing implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 关注用户id
     */
    private Long followingId;

    /**
     * 关注分组id
     */
    private Long groupId;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private UserInfo userInfo;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}