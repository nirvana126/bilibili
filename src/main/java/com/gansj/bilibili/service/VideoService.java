package com.gansj.bilibili.service;

import com.gansj.bilibili.common.PageResult;
import com.gansj.bilibili.dao.*;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.mahout.cf.taste.common.TasteException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
* @author GSJ
* @description 针对表【video(视频投稿记录表)】的数据库操作Service
* @createDate 2023-08-05 20:10:42
*/
public interface VideoService extends IService<Video> {

    void addVideo(Video video);

    PageResult<Video> pageVideoList(Integer page, Integer pageSize, String area);

    void addVideoLike(Long videoId, Long userId);

    void deleteVideoLike(Long videoId, Long userId);

    Map<String, Object> getVideoLikesCount(Long videoId, Long userId);

    void addVideoCollection(VideoCollection videoCollection, Long userId);

    void deleteVideoCollection(Long videoId, Long userId);

    Map<String, Object> getVideoCollectionsCount(Long videoId, Long userId);

    void addVideoCoins(VideoCoin videoCoin, Long userId);

    Map<String, Object> getVideoCoinsCount(Long videoId, Long userId);

    void addVideoComments(VideoComment videoComment, Long userId);

    PageResult<VideoComment> pageListVideoComments(Integer page, Integer pageSize, Long videoId);

    void addVideoViews(VideoView videoView, HttpServletRequest request);

    List<Video> recommend(Long userId) throws TasteException;
}
