package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.common.ErrorCode;
import com.gansj.bilibili.dao.FollowingGroup;
import com.gansj.bilibili.dao.User;
import com.gansj.bilibili.dao.UserFollowing;
import com.gansj.bilibili.dao.UserInfo;
import com.gansj.bilibili.dto.FollowingListRespDto;
import com.gansj.bilibili.exception.BusinessException;
import com.gansj.bilibili.service.FollowingGroupService;
import com.gansj.bilibili.service.UserFollowingService;
import com.gansj.bilibili.mapper.UserFollowingMapper;
import com.gansj.bilibili.service.UserInfoService;
import com.gansj.bilibili.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author GSJ
* @description 针对表【user_following(用户关注表)】的数据库操作Service实现
* @createDate 2023-07-23 11:15:04
*/
@Service
@Slf4j
public class UserFollowingServiceImpl extends ServiceImpl<UserFollowingMapper, UserFollowing>
    implements UserFollowingService{

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private UserService userService;

    @Resource
    private FollowingGroupService followingGroupService;

    @Override
    public List<FollowingListRespDto> getUserFollowings(Long userId) {

        QueryWrapper<UserFollowing> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<UserFollowing> userFollowingList = this.list(queryWrapper);
//        log.info(userFollowingList.toString());
        List<Long> followingIdList = userFollowingList.stream().map(UserFollowing::getFollowingId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(followingIdList)){
            return new ArrayList<>();
        }
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.in("user_id",followingIdList);
        List<UserInfo> followingUserInfoList = userInfoService.list(userInfoQueryWrapper);
        Map<Long, UserInfo> followingUsersMap = followingUserInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, userinfo -> userinfo));

        QueryWrapper<FollowingGroup> followingGroupQueryWrapper = new QueryWrapper<>();
        followingGroupQueryWrapper.eq("user_id",userId);
        List<FollowingGroup> followingGroups = followingGroupService.list(followingGroupQueryWrapper);
//        log.info(followingGroups.toString());
        List<FollowingListRespDto> result = new ArrayList<>();
        for (FollowingGroup group : followingGroups) {
            FollowingListRespDto respDto = new FollowingListRespDto();
            respDto.setId(group.getId());

            respDto.setCreateTime(group.getCreateTime());
            respDto.setUserId(group.getUserId());
            respDto.setName(group.getName());
            respDto.setType(group.getType());
//            List<UserInfo> userInfos = new ArrayList<>();
//            for (UserFollowing userFollowing : userFollowingList) {
//                if (group.getId().equals(Long.valueOf(userFollowing.getGroupId()))){
//                    userInfos.add(followingUsersMap.get(Long.valueOf(userFollowing.getFollowingId())));
//                }
//            }
            List<UserInfo> userInfos = userFollowingList.stream()
                    .filter(userFollowing -> group.getId().equals(Long.valueOf(userFollowing.getGroupId())))
                    .map(userFollowing -> followingUsersMap.get(Long.valueOf(userFollowing.getFollowingId())))
                    .collect(Collectors.toList());
            respDto.setFollowingUserInfoList(userInfos);
            result.add(respDto);
        }
        return result;
    }

    @Override
    public List<UserFollowing> getUserFans(Long userId) {

        QueryWrapper<UserFollowing> userFollowingQueryWrapper = new QueryWrapper<>();
        userFollowingQueryWrapper.eq("following_id",userId);
        List<UserFollowing> userFans = this.list(userFollowingQueryWrapper);
        if (CollectionUtils.isEmpty(userFans)){
            return new ArrayList<>();
        }
        List<Long> fanIdList = userFans.stream().map(UserFollowing::getUserId).collect(Collectors.toList());
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.in("user_id",fanIdList);
        List<UserInfo> userInfoList = userInfoService.list(userInfoQueryWrapper);
        Map<Long, UserInfo> userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getUserId, userInfo -> userInfo));

        userFollowingQueryWrapper = new QueryWrapper<>();
        userFollowingQueryWrapper.eq("user_id",userId);
        List<UserFollowing> followingList = this.list(userFollowingQueryWrapper);

        for (UserFollowing userFan : userFans) {
            if (userInfoMap.containsKey(userFan.getUserId())){
                UserInfo userInfo = userInfoMap.get(userFan.getUserId());
                userInfo.setFollowed(false);
                userFan.setUserInfo(userInfo);
            }
            //进一步判断是否互粉
            for (UserFollowing userFollowing : followingList) {
                if (Long.valueOf(userFollowing.getFollowingId()).equals(userFan.getUserId())){
                    userFan.getUserInfo().setFollowed(true);
                }
            }
        }
        return userFans;

    }

    @Override
    public Long addUserFollowingGroup(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType("3");//用户自定义分组类型设为3
        followingGroupService.save(followingGroup);
        return followingGroup.getId();

    }

    @Override
    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        QueryWrapper<FollowingGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<FollowingGroup> list = followingGroupService.list(queryWrapper);
        if (CollectionUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return list;
    }

    @Override
    @Transactional
    public void addUserFollowing(UserFollowing userFollowing) {
        Long followingId = userFollowing.getFollowingId();
        User followingUser = userService.getById(followingId);
        if (followingUser==null){
            throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"所关注的用户不存在");
        }
        Long groupId = userFollowing.getGroupId();
        if (groupId==null){
            QueryWrapper<FollowingGroup> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type","2");
            FollowingGroup group = followingGroupService.getOne(queryWrapper);
            userFollowing.setGroupId(group.getId());//设为默认分组
        }else {
            FollowingGroup group = followingGroupService.getById(groupId);
            if (group==null){
                throw new BusinessException(ErrorCode.REQUEST_PARAMS_ERROR,"添加的分组不存在");
            }
        }
        QueryWrapper<UserFollowing> userFollowingQueryWrapper = new QueryWrapper<>();
        userFollowingQueryWrapper.eq("following_id",followingId).eq("user_id",userFollowing.getUserId());
        this.remove(userFollowingQueryWrapper);
        userFollowing.setCreateTime(new Date());
        this.save(userFollowing);
    }
}




