package com.gansj.bilibili.service;

import com.gansj.bilibili.dao.AuthRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
* @author GSJ
* @description 针对表【auth_role_menu(权限控制--角色页面菜单关联表)】的数据库操作Service
* @createDate 2023-08-12 16:14:51
*/
public interface AuthRoleMenuService extends IService<AuthRoleMenu> {

    List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet);
}
