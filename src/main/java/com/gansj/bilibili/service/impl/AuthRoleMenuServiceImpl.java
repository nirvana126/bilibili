package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.AuthRoleMenu;
import com.gansj.bilibili.service.AuthRoleMenuService;
import com.gansj.bilibili.mapper.AuthRoleMenuMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
* @author GSJ
* @description 针对表【auth_role_menu(权限控制--角色页面菜单关联表)】的数据库操作Service实现
* @createDate 2023-08-12 16:14:51
*/
@Service
public class AuthRoleMenuServiceImpl extends ServiceImpl<AuthRoleMenuMapper, AuthRoleMenu>
    implements AuthRoleMenuService{

    @Resource
    private AuthRoleMenuMapper authRoleMenuMapper;

    @Override
    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuMapper.getAuthRoleMenusByRoleIds(roleIdSet);
    }
}




