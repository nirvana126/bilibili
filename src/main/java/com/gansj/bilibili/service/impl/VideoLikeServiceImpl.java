package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.VideoLike;
import com.gansj.bilibili.service.VideoLikeService;
import com.gansj.bilibili.mapper.VideoLikeMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【video_like(视频点赞表)】的数据库操作Service实现
* @createDate 2023-08-05 22:02:28
*/
@Service
public class VideoLikeServiceImpl extends ServiceImpl<VideoLikeMapper, VideoLike>
    implements VideoLikeService{

}




