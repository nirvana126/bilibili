package com.gansj.bilibili.mapper;

import com.gansj.bilibili.dao.VideoView;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

/**
* @author GSJ
* @description 针对表【video_view(视频观看记录表)】的数据库操作Mapper
* @createDate 2023-08-10 20:14:34
* @Entity com.gansj.bilibili.dao.VideoView
*/
public interface VideoViewMapper extends BaseMapper<VideoView> {

    VideoView getVideoViews(Map<String, Object> params);
}




