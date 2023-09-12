package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.VideoView;
import com.gansj.bilibili.service.VideoViewService;
import com.gansj.bilibili.mapper.VideoViewMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【video_view(视频观看记录表)】的数据库操作Service实现
* @createDate 2023-08-10 20:14:34
*/
@Service
public class VideoViewServiceImpl extends ServiceImpl<VideoViewMapper, VideoView>
    implements VideoViewService{

}




