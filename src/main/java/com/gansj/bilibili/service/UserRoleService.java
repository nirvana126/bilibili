package com.gansj.bilibili.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gansj.bilibili.dao.UserRole;

import java.util.List;

/**
* @author GSJ
* @description 针对表【user_role(用户角色关联表)】的数据库操作Service
* @createDate 2023-08-12 15:04:17
*/
public interface UserRoleService extends IService<UserRole> {

    List<UserRole> getUserRoleByUserId(Long userId);
}
