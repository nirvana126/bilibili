package com.gansj.bilibili.controller;


import com.gansj.bilibili.common.BaseResponse;
import com.gansj.bilibili.common.ErrorCode;
import com.gansj.bilibili.common.ResultUtils;
import com.gansj.bilibili.dao.User;
import com.gansj.bilibili.dao.UserInfo;
import com.gansj.bilibili.manager.UserSupport;
import com.gansj.bilibili.service.UserService;
import com.gansj.bilibili.utils.RSAUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private UserSupport userSupport;

    @GetMapping("/rsa-pks")
    public BaseResponse<String> getRsaPublicKey(){
        String publicKeyStr = RSAUtil.getPublicKeyStr();
        return ResultUtils.success(publicKeyStr);
    }

    @PostMapping("/users")
    public BaseResponse<String> userRegister(@RequestBody User user){
        userService.userRegister(user);
        return ResultUtils.success("用户注册成功");
    }

    @PostMapping("/user-login")
    public BaseResponse<String> userLogin(@RequestBody User user) throws Exception {
        String token = userService.userLogin(user);
        return ResultUtils.success("登录成功");
    }

    @PutMapping("/userinfo-update")
    @Operation(summary = "更新用户信息")
    public BaseResponse<String> updateUserInfo(@RequestBody UserInfo userInfo){
        Long userId = userSupport.getCurrentUserId();
        userInfo.setUserId(userId);
        boolean isSuccess = userService.updateUserInfo(userInfo);
        if (!isSuccess){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"用户信息更新失败！");
        }
        return ResultUtils.success("更新成功！");
    }


}
