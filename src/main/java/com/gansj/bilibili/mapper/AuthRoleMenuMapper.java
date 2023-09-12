package com.gansj.bilibili.mapper;

import com.gansj.bilibili.dao.AuthRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Set;

/**
* @author GSJ
* @description 针对表【auth_role_menu(权限控制--角色页面菜单关联表)】的数据库操作Mapper
* @createDate 2023-08-12 16:14:51
* @Entity com.gansj.bilibili.dao.AuthRoleMenu
*/
public interface AuthRoleMenuMapper extends BaseMapper<AuthRoleMenu> {

    List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet);
}




