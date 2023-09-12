package com.gansj.bilibili.dto;

import com.gansj.bilibili.dao.FollowingGroup;
import com.gansj.bilibili.dao.UserInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户关注列表的分组汇总信息
 */
public class FollowingListRespDto extends FollowingGroup implements Serializable {

    private static final long serialVersionUID = -6844843239612314024L;

    private List<UserInfo> followingUserInfoList;

    public FollowingListRespDto(List<UserInfo> followingUserInfoList) {
        this.followingUserInfoList = followingUserInfoList;
    }

    public FollowingListRespDto() {
    }

    public List<UserInfo> getFollowingUserInfoList() {
        return followingUserInfoList;
    }

    public void setFollowingUserInfoList(List<UserInfo> followingUserInfoList) {
        this.followingUserInfoList = followingUserInfoList;
    }
}
