package com.gansj.bilibili.service;

import com.gansj.bilibili.dao.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【user_info(用户基本信息表)】的数据库操作Service
* @createDate 2023-07-22 09:07:26
*/
public interface UserInfoService extends IService<UserInfo> {

    UserInfo getInfoByUserId(Long userId);
}
