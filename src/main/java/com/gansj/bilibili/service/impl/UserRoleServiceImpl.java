package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.UserRole;
import com.gansj.bilibili.service.UserRoleService;
import com.gansj.bilibili.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author GSJ
* @description 针对表【user_role(用户角色关联表)】的数据库操作Service实现
* @createDate 2023-08-12 15:04:17
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> getUserRoleByUserId(Long userId) {

        return userRoleMapper.getUserRoleByUserId(userId);
    }
}




