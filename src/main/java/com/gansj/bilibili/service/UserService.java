package com.gansj.bilibili.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gansj.bilibili.dao.User;
import com.gansj.bilibili.dao.UserInfo;

public interface UserService extends IService<User> {
    void userRegister(User user);

    String userLogin(User user) throws Exception;

    boolean updateUserInfo(UserInfo userInfo);
}
