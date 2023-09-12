package com.gansj.bilibili.controller;

import com.gansj.bilibili.common.BaseResponse;
import com.gansj.bilibili.common.PageResult;
import com.gansj.bilibili.common.ResultUtils;
import com.gansj.bilibili.dao.*;
import com.gansj.bilibili.manager.UserSupport;
import com.gansj.bilibili.service.VideoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.mahout.cf.taste.common.TasteException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Resource
    private VideoService videoService;

    @Resource
    private UserSupport userSupport;

    @PostMapping("/add")
    @ApiOperation(value = "视频投稿接口")
    public BaseResponse<String> addVideo(@RequestBody Video video){

        Long userId = userSupport.getCurrentUserId();
        video.setUserId(userId);
        videoService.addVideo(video);
        return ResultUtils.success();
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询视频列表")
    public BaseResponse<PageResult<Video>> pageVideoList(Integer page,Integer pageSize,String area){
        PageResult<Video> videoList = videoService.pageVideoList(page,pageSize,area);
        return ResultUtils.success(videoList);
    }

    @PostMapping("/like")
    @ApiOperation(value = "视频点赞")
    public BaseResponse<String> addVideoLike(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoLike(videoId,userId);
        return ResultUtils.success();
    }

    @DeleteMapping ("/like-cancel")
    @ApiOperation(value = "取消视频点赞")
    public BaseResponse<String> deleteVideoLike(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoLike(videoId,userId);
        return ResultUtils.success();
    }

    @GetMapping ("/like-count")
    @ApiOperation(value = "查询视频点赞数")
    public BaseResponse<Map<String,Object>> getVideoLikesCount(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        Map<String,Object> res= videoService.getVideoLikesCount(videoId,userId);
        return ResultUtils.success(res);
    }

    @PostMapping("/add-collection")
    @ApiOperation(value = "视频收藏")
    public BaseResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoCollection(videoCollection,userId);
        return ResultUtils.success();
    }

    @DeleteMapping ("/collection-cancel")
    @ApiOperation(value = "取消视频收藏")
    public BaseResponse<String> deleteVideoCollection(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        videoService.deleteVideoCollection(videoId,userId);
        return ResultUtils.success();
    }

    @GetMapping ("/collections-count")
    @ApiOperation(value = "查询视频收藏数")
    public BaseResponse<Map<String,Object>> getVideoCollectionsCount(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        Map<String,Object> res= videoService.getVideoCollectionsCount(videoId,userId);
        return ResultUtils.success(res);
    }

    @PostMapping("/add-coins")
    @ApiOperation(value = "视频投币")
    public BaseResponse<String> addVideoCoins(@RequestBody VideoCoin videoCoin){
//        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoCoins(videoCoin,1L);
        return ResultUtils.success();
    }

    @GetMapping ("/coins-count")
    @ApiOperation(value = "查询视频投币数")
    public BaseResponse<Map<String,Object>> getVideoCoinsCount(@RequestParam Long videoId){
        Long userId = userSupport.getCurrentUserId();
        Map<String,Object> res= videoService.getVideoCoinsCount(videoId,userId);
        return ResultUtils.success(res);
    }

    @PostMapping ("/add-comments")
    @ApiOperation(value = "添加视频评论")
    public BaseResponse<String> addVideoComments(@RequestBody VideoComment videoComment){
        Long userId = userSupport.getCurrentUserId();
        videoService.addVideoComments(videoComment,userId);
        return ResultUtils.success();
    }

    @GetMapping("/page-comments")
    @ApiOperation(value = "分页查询视频评论")
    public BaseResponse<PageResult<VideoComment>> pageListVideoComments(@RequestParam Integer page,@RequestParam Integer pageSize,@RequestParam Long videoId){
        PageResult<VideoComment> videoComments = videoService.pageListVideoComments(page,pageSize,videoId);
        return ResultUtils.success(videoComments);
    }

    @GetMapping("/add-views")
    @ApiOperation(value = "添加视频观看记录")
    public BaseResponse<String> addVideoViews(@RequestBody VideoView videoView, HttpServletRequest request){

        try {
            Long userId = userSupport.getCurrentUserId();
            videoView.setUserId(userId);
            videoService.addVideoViews(videoView,request);
        }catch (Exception e){
            videoService.addVideoViews(videoView,request);
        }
        return ResultUtils.success();
    }

    @GetMapping("/recommendations")
    @ApiOperation(value = "根据用户行为推荐视频")
    public BaseResponse<List<Video>> recommend() throws TasteException {
        Long userId = userSupport.getCurrentUserId();
        List<Video> list = videoService.recommend(userId);
        return ResultUtils.success(list);
    }



}
