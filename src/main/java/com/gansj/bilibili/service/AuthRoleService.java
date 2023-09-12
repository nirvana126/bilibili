package com.gansj.bilibili.service;

import com.gansj.bilibili.dao.AuthRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gansj.bilibili.dao.AuthRoleElementOperation;
import com.gansj.bilibili.dao.AuthRoleMenu;

import java.util.List;
import java.util.Set;

/**
* @author GSJ
* @description 针对表【auth_role(权限控制--角色表)】的数据库操作Service
* @createDate 2023-08-12 15:15:48
*/
public interface AuthRoleService extends IService<AuthRole> {

    List<AuthRoleElementOperation> getRoleElementOperations(Set<Long> roleIdSet);

    List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet);
}
