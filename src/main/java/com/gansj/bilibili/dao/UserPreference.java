package com.gansj.bilibili.dao;

import lombok.Data;

import java.util.Date;
@Data
public class UserPreference {

    private Long id;

    private Long userId;

    private Long videoId;

    private String operationType;

    private Date createTime;

    private Float value;
}
