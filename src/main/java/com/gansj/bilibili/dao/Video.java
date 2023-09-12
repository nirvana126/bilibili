package com.gansj.bilibili.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;
import org.w3c.dom.ls.LSInput;

/**
 * 视频投稿记录表
 * @TableName video
 */
@TableName(value ="video")
@Data
//@Document(indexName = "videos")
public class Video implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    @Id
    private Long id;

    /**
     * 用户id
     */
//    @Field(type = FieldType.Long)
    private Long userId;

    /**
     * 视频链接
     */
    private String url;

    /**
     * 封面链接
     */
    private String thumbnail;

    /**
     * 视频标题
     */
//    @Field(type = FieldType.Text)
    private String title;

    /**
     * 视频类型：0原创，1转载
     */
    private String type;

    /**
     * 视频时长
     */
    private String duration;

    /**
     * 所在分区:0鬼畜，1音乐，2电影
     */
    private String area;

    /**
     * 视频简介
     */
//    @Field(type = FieldType.Text)
    private String description;

    /**
     * 视频的标签列表
     */
    @TableField(exist = false)
    private List<VideoTag> videoTagList;

    /**
     * 创建时间
     */
//    @Field(type = FieldType.Date)
    private Date createTime;

    /**
     * 更新时间
     */
//    @Field(type = FieldType.Date)
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}