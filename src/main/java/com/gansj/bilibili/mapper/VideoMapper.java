package com.gansj.bilibili.mapper;

import com.gansj.bilibili.dao.UserPreference;
import com.gansj.bilibili.dao.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
* @author GSJ
* @description 针对表【video(视频投稿记录表)】的数据库操作Mapper
* @createDate 2023-08-05 20:10:42
* @Entity com.gansj.bilibili.dao.Video
*/
public interface VideoMapper extends BaseMapper<Video> {

    List<UserPreference> getAllUserPreference();

}




