package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.VideoComment;
import com.gansj.bilibili.service.VideoCommentService;
import com.gansj.bilibili.mapper.VideoCommentMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【video_comment(视频评论表)】的数据库操作Service实现
* @createDate 2023-08-06 20:15:26
*/
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment>
    implements VideoCommentService{

}




