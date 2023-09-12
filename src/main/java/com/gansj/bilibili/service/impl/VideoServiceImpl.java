package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.common.ErrorCode;
import com.gansj.bilibili.common.PageResult;
import com.gansj.bilibili.dao.*;
import com.gansj.bilibili.exception.BusinessException;
import com.gansj.bilibili.mapper.VideoViewMapper;
import com.gansj.bilibili.service.*;
import com.gansj.bilibili.mapper.VideoMapper;
import com.gansj.bilibili.service.VideoCommentService;
import com.gansj.bilibili.utils.IpUtil;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericItemPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author GSJ
* @description 针对表【video(视频投稿记录表)】的数据库操作Service实现
* @createDate 2023-08-05 20:10:42
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

    @Resource
    private VideoTagService videoTagService;

    @Resource
    private VideoLikeService videoLikeService;

    @Resource
    private VideoCollectionService videoCollectionService;

    @Resource
    private VideoCoinService videoCoinService;

    @Resource
    private UserCoinService userCoinService;

    @Resource
    private VideoCommentService videoCommentService;

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private VideoViewMapper videoViewMapper;

    @Resource
    private VideoMapper videoMapper;

    @Override
    @Transactional
    public void addVideo(Video video) {
        this.save(video);
        Long videoId = video.getId();
        List<VideoTag> videoTagList = video.getVideoTagList();
        if (videoTagList != null) {
            videoTagList.forEach(item->{
                item.setVideoId(videoId);
            });
        }
        videoTagService.saveBatch(videoTagList);
    }

    @Override
    public PageResult<Video> pageVideoList(Integer page, Integer pageSize, String area) {
        if (page==null || pageSize==null){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR);
        }
        String lastSql = "limit "+(page-1)*pageSize +","+pageSize;
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(area)){
            queryWrapper.eq("area",area);
        }
        //先查出数量
        long count = this.count(queryWrapper);
        List<Video> videoList = new ArrayList<>();
        if (count > 0){
            //再查出对应页数的数据列表
            LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(StringUtils.isNotBlank(area),Video::getArea,area).orderByDesc(Video::getId).last(lastSql);
            videoList= this.list(lambdaQueryWrapper);
        }
        return new PageResult<Video>(count,videoList);
    }

    @Override
    public void addVideoLike(Long videoId, Long userId) {
        Video video = this.getById(videoId);
        if (video == null){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"非法视频！");
        }
        QueryWrapper<VideoLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId);
        VideoLike like = videoLikeService.getOne(queryWrapper);
        if (like!=null){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"已经赞过！");
        }
        VideoLike videoLike = new VideoLike();
        videoLike.setVideoId(videoId);
        videoLike.setUserId(userId);
        videoLike.setCreateTime(new Date());
        videoLikeService.save(videoLike);
    }

    @Override
    public void deleteVideoLike(Long videoId, Long userId) {

        QueryWrapper<VideoLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId).eq("user_id",userId);
        videoLikeService.remove(queryWrapper);
    }

    @Override
    public Map<String, Object> getVideoLikesCount(Long videoId, Long userId) {

        QueryWrapper<VideoLike> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId);
        long count = videoLikeService.count(queryWrapper);

        QueryWrapper<VideoLike> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("video_id",videoId).eq("user_id",userId);
        VideoLike videoLike = videoLikeService.getOne(queryWrapper1);
        boolean like = videoLike!=null ;
        Map<String,Object> res = new HashMap<>();
        res.put("count",count);
        res.put("like",like);
        return res;
    }

    @Override
    @Transactional
    public void addVideoCollection(VideoCollection videoCollection, Long userId) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if (videoId==null || groupId==null){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"参数异常!");
        }
        Video video = this.getById(videoId);
        if (video==null){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"非法视频！");
        }
        QueryWrapper<VideoCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId).eq("user_id",userId);
        videoCollectionService.remove(queryWrapper);
        videoCollection.setUserId(userId);
        videoCollection.setCreateTime(new Date());
        videoCollectionService.save(videoCollection);
    }

    @Override
    public void deleteVideoCollection(Long videoId, Long userId) {
        QueryWrapper<VideoCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId).eq("user_id",userId);
        videoCollectionService.remove(queryWrapper);
    }

    @Override
    public Map<String, Object> getVideoCollectionsCount(Long videoId, Long userId) {
        QueryWrapper<VideoCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId);
        long count = videoCollectionService.count(queryWrapper);
        QueryWrapper<VideoCollection> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("video_id",videoId).eq("user_id",userId);
        VideoCollection collection = videoCollectionService.getOne(queryWrapper1);
        boolean collect = collection != null;
        Map<String,Object> res = new HashMap<>();
        res.put("count",count);
        res.put("collect",collect);
        return res;
    }

    @Override
    @Transactional
    public void addVideoCoins(VideoCoin videoCoin, Long userId) {
        Long videoId = videoCoin.getVideoId();
        int amount = videoCoin.getAmount();
        if (videoId == null) {
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"参数异常！");
        }
        Video video = this.getById(videoId);
        if (video == null) {
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"视频异常！");
        }
        QueryWrapper<UserCoin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        UserCoin userCoin = userCoinService.getOne(queryWrapper);
        Long userCoinAmount = userCoin.getAmount();
        long userCoinTotal = userCoinAmount == null ? 0 : userCoinAmount;
        if (userCoinTotal < amount){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"硬币数量不足！");
        }
        QueryWrapper<VideoCoin> queryWrapper1=new QueryWrapper<>();
        queryWrapper1.eq("video_id",videoId).eq("user_id",userId);
        VideoCoin dbVideoCoin = videoCoinService.getOne(queryWrapper1);
        if (dbVideoCoin == null) {
            videoCoin.setUserId(userId);
            videoCoin.setAmount(amount);
            videoCoin.setCreateTime(new Date());
            videoCoinService.save(videoCoin);
        }else {
            Integer dbVideoCoinAmount = dbVideoCoin.getAmount();
            dbVideoCoinAmount+=amount;
            videoCoin.setAmount(dbVideoCoinAmount);
            videoCoin.setUpdateTime(new Date());
            videoCoinService.updateVideoCoin(videoCoin);
        }
        //最后更新用户总硬币数
        UpdateWrapper<UserCoin> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("amount",userCoinTotal-amount).eq("user_id",userId);
        userCoinService.update(updateWrapper);
    }

    @Override
    public Map<String, Object> getVideoCoinsCount(Long videoId, Long userId) {

        Long count = videoCoinService.getVideoCoinsAmount(videoId);
        QueryWrapper<VideoCoin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId).eq("user_id",userId);
        VideoCoin videoCoin = videoCoinService.getOne(queryWrapper);
        boolean coin = videoCoin != null;
        Map<String,Object> map = new HashMap<>();
        map.put("count",count);
        map.put("coin",coin);
        return map;

    }

    @Override
    public void addVideoComments(VideoComment videoComment, Long userId) {
        if (videoComment == null) {
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR);
        }
        Long videoId = videoComment.getVideoId();
        if (videoId == null) {
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"参数异常！");
        }
        Video video = this.getById(videoId);
        if (video == null) {
            throw new BusinessException(ErrorCode.VIDEO_ERROR);
        }
        videoComment.setUserId(userId);
        videoComment.setCreateTime(new Date());
        videoCommentService.save(videoComment);
    }

    @Override
    public PageResult<VideoComment> pageListVideoComments(Integer page, Integer pageSize, Long videoId) {

        if (page==null || pageSize == null || videoId == null) {
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"参数异常！");
        }
        Video video = this.getById(videoId);
        if (video == null) {
            throw new BusinessException(ErrorCode.VIDEO_ERROR);
        }
        int start = (page-1)*pageSize;
        String sqlStr ="limit "+start+","+pageSize;
        //先查出当前视频的所有一级评论数量
        QueryWrapper<VideoComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_id",videoId).isNull("root_id");
        long total = videoCommentService.count(queryWrapper);
        List<VideoComment> list = new ArrayList<>();
        if (total > 0){
            //若有，则查出所有的一级评论
            QueryWrapper<VideoComment> qw1 = new QueryWrapper<>();
            qw1.eq("video_id",videoId).isNull("root_id").orderByDesc("id").last(sqlStr);
            list = videoCommentService.list(qw1);
            //取所有一级评论的id
            List<Long> parentIdList = list.stream().map(VideoComment::getId).collect(Collectors.toList());
            //批量查询所有二级评论
            QueryWrapper<VideoComment> qw2 = new QueryWrapper<>();
            qw2.in("root_id",parentIdList).orderByAsc("id");
            List<VideoComment> childList = videoCommentService.list(qw2);
            //查询所有参与评论的用户信息（包括一级、二级评论的用户）,存入Map中便于后续取用 todo
            Set<Long> childUserIds = childList.stream().map(VideoComment::getReplyUserId).collect(Collectors.toSet());
            Set<Long> userIdSet = list.stream().map(VideoComment::getUserId).collect(Collectors.toSet());
            userIdSet.addAll(childUserIds);
            QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
            userInfoQueryWrapper.in("user_id",userIdSet);
            List<UserInfo> userInfoList = userInfoService.list(userInfoQueryWrapper);
            System.out.println(userInfoList);
            Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));
            //组装返回结果
            list.forEach(videoComment -> {
                Long id = videoComment.getId();
                List<VideoComment> childComments = new ArrayList<>();
                childList.forEach(child->{
                    if (id.equals(child.getRootId())){
                        child.setUserInfo(userInfoMap.get(child.getUserId()));
                        child.setReplyUserInfo(userInfoMap.get(child.getReplyUserId()));
                        childComments.add(child);
                    }
                });
                videoComment.setChildCommentsList(childComments);
                videoComment.setUserInfo(userInfoMap.get(videoComment.getUserId()));
            });
        }
        return new PageResult<>(total, list);
    }

    @Override
    public void addVideoViews(VideoView videoView, HttpServletRequest request) {
        Long userId = videoView.getUserId();
        Long videoId = videoView.getVideoId();
        //生成clientId
        String agent = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(agent);
        String clientId = String.valueOf(userAgent.getId());
        //获取IP
        String ip = IpUtil.getIP(request);
        Map<String,Object> params = new HashMap<>();
        params.put("videoId",videoId);
        //若是已登录用户，则传入用户ID
        if (userId != null) {
            params.put("userId",userId);
        }else {
            params.put("ip",ip);
            params.put("clientId",clientId);
        }
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        params.put("today",dateFormat.format(now));
        //添加观看记录
        //先查询数据库是否有记录，注意查询条件的编写
        VideoView dbVideoView = videoViewMapper.getVideoViews(params);
        if (dbVideoView==null){
            videoView.setIp(ip);
            videoView.setClientId(clientId);
            videoView.setCreateTime(new Date());
            videoViewMapper.insert(videoView);
        }
    }

    @Override
    public List<Video> recommend(Long userId) throws TasteException {
        List<UserPreference> list=videoMapper.getAllUserPreference();
        //创建数据模型
        DataModel dataModel=this.createDataModel(list);
        //获取用户相似度
        UserSimilarity similarity=new UncenteredCosineSimilarity(dataModel);
        System.out.println(similarity.userSimilarity(11,12));
        //用户邻居
        UserNeighborhood userNeighborhood=new NearestNUserNeighborhood(2,similarity,dataModel);
        long[] ar=userNeighborhood.getUserNeighborhood(userId);
        //构建推荐器
        Recommender recommender=new GenericUserBasedRecommender(dataModel,userNeighborhood,similarity);
        //推荐商品
        List<RecommendedItem> recommendedItems=recommender.recommend(userId,5);
        List<Long> itemIds=recommendedItems.stream().map(RecommendedItem::getItemID).collect(Collectors.toList());
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",itemIds);
        return this.list(queryWrapper);
    }

    private DataModel createDataModel(List<UserPreference> userPreferenceList){
        FastByIDMap<PreferenceArray> fastByIDMap=new FastByIDMap<>();
        Map<Long,List<UserPreference>> map=userPreferenceList.stream().collect(Collectors.groupingBy(UserPreference::getUserId));
        Collection<List<UserPreference>> list=map.values();
        for(List<UserPreference> userPreferences:list){
            GenericPreference[] array=new GenericPreference[userPreferences.size()];
            for(int i=0;i<userPreferences.size();i++){
                UserPreference userPreference=userPreferenceList.get(i);
                GenericPreference item=new GenericPreference(userPreference.getUserId(),userPreference.getVideoId(),userPreference.getValue());
                array[i]=item;
            }
            fastByIDMap.put(array[0].getUserID(),new GenericItemPreferenceArray(Arrays.asList(array)));

        }
        return new GenericDataModel(fastByIDMap);
    }
}




