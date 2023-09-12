package com.gansj.bilibili.controller;

import com.gansj.bilibili.common.BaseResponse;
import com.gansj.bilibili.common.ResultUtils;
import com.gansj.bilibili.constant.AuthRoleConstant;
import com.gansj.bilibili.core.annotation.ApiLimitedRole;
import com.gansj.bilibili.core.annotation.DataLimited;
import com.gansj.bilibili.dao.UserMoment;
import com.gansj.bilibili.manager.UserSupport;
import com.gansj.bilibili.service.UserMomentService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户动态有关接口
 */
@RestController
@Slf4j
@RequestMapping("/user-moment")
public class UserMomentController {

    @Resource
    private UserMomentService userMomentService;

    @Resource
    private UserSupport userSupport;

    @PostMapping("/add")
    @ApiOperation(value = "添加用户动态")
    @ApiLimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV0})
    @DataLimited
    public BaseResponse<String> addUserMoment(@RequestBody UserMoment userMoment) throws Exception {
        Long userId = userSupport.getCurrentUserId();
        userMoment.setUserId(userId);
        userMomentService.addUserMoment(userMoment);
        return ResultUtils.success("add success!");
    }

    @GetMapping("/get-subscribed-moments")
    @ApiOperation(value = "查询关注的用户动态列表")
    public BaseResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId = userSupport.getCurrentUserId();
        List<UserMoment> userMoments=userMomentService.getUserSubscribedMoments(userId);
        return ResultUtils.success(userMoments);
    }


}
