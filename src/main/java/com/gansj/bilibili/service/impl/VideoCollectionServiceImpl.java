package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.VideoCollection;
import com.gansj.bilibili.service.VideoCollectionService;
import com.gansj.bilibili.mapper.VideoCollectionMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【video_collection(视频收藏表)】的数据库操作Service实现
* @createDate 2023-08-06 11:50:31
*/
@Service
public class VideoCollectionServiceImpl extends ServiceImpl<VideoCollectionMapper, VideoCollection>
    implements VideoCollectionService{

}




