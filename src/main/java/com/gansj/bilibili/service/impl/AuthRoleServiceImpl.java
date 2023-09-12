package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.AuthRole;
import com.gansj.bilibili.dao.AuthRoleElementOperation;
import com.gansj.bilibili.dao.AuthRoleMenu;
import com.gansj.bilibili.service.AuthRoleElementOperationService;
import com.gansj.bilibili.service.AuthRoleMenuService;
import com.gansj.bilibili.service.AuthRoleService;
import com.gansj.bilibili.mapper.AuthRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
* @author GSJ
* @description 针对表【auth_role(权限控制--角色表)】的数据库操作Service实现
* @createDate 2023-08-12 15:15:48
*/
@Service
public class AuthRoleServiceImpl extends ServiceImpl<AuthRoleMapper, AuthRole>
    implements AuthRoleService{

    @Resource
    private AuthRoleMapper authRoleMapper;

    @Resource
    private AuthRoleElementOperationService authRoleElementOperationService;

    @Resource
    private AuthRoleMenuService authRoleMenuService;


    @Override
    public List<AuthRoleElementOperation> getRoleElementOperations(Set<Long> roleIdSet) {
        return authRoleElementOperationService.getRoleElementOperations(roleIdSet);
    }

    @Override
    public List<AuthRoleMenu> getAuthRoleMenusByRoleIds(Set<Long> roleIdSet) {
        return authRoleMenuService.getAuthRoleMenusByRoleIds(roleIdSet);

    }
}




