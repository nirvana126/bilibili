package com.gansj.bilibili.service;

import com.gansj.bilibili.dao.FollowingGroup;
import com.gansj.bilibili.dao.UserFollowing;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gansj.bilibili.dto.FollowingListRespDto;

import java.util.List;

/**
* @author GSJ
* @description 针对表【user_following(用户关注表)】的数据库操作Service
* @createDate 2023-07-23 11:15:04
*/
public interface UserFollowingService extends IService<UserFollowing> {

    List<FollowingListRespDto> getUserFollowings(Long userId);

    List<UserFollowing> getUserFans(Long userId);

    Long addUserFollowingGroup(FollowingGroup followingGroup);

    List<FollowingGroup> getUserFollowingGroups(Long userId);

    void addUserFollowing(UserFollowing userFollowing);

}
