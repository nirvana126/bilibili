package com.gansj.bilibili.service;

import com.gansj.bilibili.dao.UserMoment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author GSJ
* @description 针对表【user_moment(用户动态表)】的数据库操作Service
* @createDate 2023-07-24 15:47:10
*/
public interface UserMomentService extends IService<UserMoment> {

    void addUserMoment(UserMoment userMoment) throws Exception;

    List<UserMoment> getUserSubscribedMoments(Long userId);
}
