package com.gansj.bilibili.controller;


import com.gansj.bilibili.common.BaseResponse;
import com.gansj.bilibili.common.ResultUtils;
import com.gansj.bilibili.dao.FollowingGroup;
import com.gansj.bilibili.dao.UserFollowing;
import com.gansj.bilibili.dto.FollowingListRespDto;
import com.gansj.bilibili.manager.UserSupport;
import com.gansj.bilibili.service.UserFollowingService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
public class UserFollowingController {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserFollowingService userFollowingService;

    @PostMapping("/user-followings")
    @ApiOperation(value = "添加用户关注")
    public BaseResponse<String> addUserFollowing(@RequestBody UserFollowing userFollowing){
        Long userId = userSupport.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowing(userFollowing);
        return ResultUtils.success();
    }

    @GetMapping("/user-followings-list")
    @ApiOperation(value = "查询用户关注列表")
    public BaseResponse<List<FollowingListRespDto>> getUserFollowings(){
//        Long userId = userSupport.getCurrentUserId();
        List<FollowingListRespDto> res = userFollowingService.getUserFollowings(1L);
        return ResultUtils.success(res);
    }

    @GetMapping("/user-fans")
    @ApiOperation(value = "查询用户粉丝列表")
    public BaseResponse<List<UserFollowing>> getUserFans(){
//        Long userId = userSupport.getCurrentUserId();
        List<UserFollowing> userFans=userFollowingService.getUserFans(1L);
        return ResultUtils.success(userFans);
    }

    @PostMapping("/user-following-group")
    @ApiOperation(value = "添加用户关注分组")
    public BaseResponse<Long> addUserFollowingGroup(@RequestBody FollowingGroup followingGroup){

        Long userId = userSupport.getCurrentUserId();
        followingGroup.setUserId(userId);
        Long groupId = userFollowingService.addUserFollowingGroup(followingGroup);
        return ResultUtils.success(groupId);
    }

    @GetMapping("/following-groups")
    @ApiOperation(value = "获取用户的所有关注分组")
    public BaseResponse<List<FollowingGroup>> getUserFollowingGroups(){

        Long userId = userSupport.getCurrentUserId();
        List<FollowingGroup> followingGroups= userFollowingService.getUserFollowingGroups(userId);
        return ResultUtils.success(followingGroups);
    }


}
