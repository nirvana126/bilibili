package com.gansj.bilibili.controller;

import com.gansj.bilibili.common.BaseResponse;
import com.gansj.bilibili.common.ResultUtils;
import com.gansj.bilibili.dto.UserAuthoritiesRespDto;
import com.gansj.bilibili.manager.UserAuthManager;
import com.gansj.bilibili.manager.UserSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserAuthController {

    @Resource
    private UserSupport userSupport;

    @Resource
    private UserAuthManager userAuthManager;

    @GetMapping("/user-authorities")
    public BaseResponse<UserAuthoritiesRespDto> getUserAuthorities(){
//        Long userId = userSupport.getCurrentUserId();
        UserAuthoritiesRespDto userAuthoritiesRespDto=userAuthManager.getUserAuthorities(1L);
        return ResultUtils.success(userAuthoritiesRespDto);
    }

    @GetMapping("/user-authorities-async")
    public BaseResponse<UserAuthoritiesRespDto> getUserAuthoritiesAsync(){
//        Long userId = userSupport.getCurrentUserId();
        UserAuthoritiesRespDto userAuthoritiesRespDto=userAuthManager.asyncGetUserAuthorities(1L);
        return ResultUtils.success(userAuthoritiesRespDto);
    }

}
