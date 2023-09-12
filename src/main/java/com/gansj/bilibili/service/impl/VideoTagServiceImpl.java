package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.VideoTag;
import com.gansj.bilibili.service.VideoTagService;
import com.gansj.bilibili.mapper.VideoTagMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【video_tag(视频标签关联表)】的数据库操作Service实现
* @createDate 2023-08-05 20:10:42
*/
@Service
public class VideoTagServiceImpl extends ServiceImpl<VideoTagMapper, VideoTag>
    implements VideoTagService{

}




