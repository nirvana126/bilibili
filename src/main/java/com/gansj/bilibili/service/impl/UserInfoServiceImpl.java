package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.UserInfo;
import com.gansj.bilibili.service.UserInfoService;
import com.gansj.bilibili.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【user_info(用户基本信息表)】的数据库操作Service实现
* @createDate 2023-07-22 09:07:26
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

    @Override
    public UserInfo getInfoByUserId(Long userId) {

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        UserInfo userInfo = this.getOne(queryWrapper);
        return userInfo;
    }
}




