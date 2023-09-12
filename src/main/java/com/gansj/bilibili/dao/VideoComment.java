package com.gansj.bilibili.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 视频评论表
 * @TableName video_comment
 */
@TableName(value ="video_comment")
@Data
public class VideoComment implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 视频id
     */
    private Long videoId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 评论
     */
    private String comment;

    /**
     * 回复用户id
     */
    private Long replyUserId;

    /**
     * 根结点评论id
     */
    private Long rootId;

    /**
     * 二级评论
     */
    @TableField(exist = false)
    private List<VideoComment> childCommentsList;

    /**
     * 用户的信息
     */
    @TableField(exist = false)
    private UserInfo userInfo;

    /**
     * 回复用户的信息
     */
    @TableField(exist = false)
    private UserInfo replyUserInfo;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}