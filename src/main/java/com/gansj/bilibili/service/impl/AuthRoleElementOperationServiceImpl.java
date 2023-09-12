package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.AuthRoleElementOperation;
import com.gansj.bilibili.service.AuthRoleElementOperationService;
import com.gansj.bilibili.mapper.AuthRoleElementOperationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
* @author GSJ
* @description 针对表【auth_role_element_operation(权限控制--角色与元素操作关联表)】的数据库操作Service实现
* @createDate 2023-08-12 15:58:17
*/
@Service
public class AuthRoleElementOperationServiceImpl extends ServiceImpl<AuthRoleElementOperationMapper, AuthRoleElementOperation>
    implements AuthRoleElementOperationService{

    @Resource
    private AuthRoleElementOperationMapper authRoleElementOperationMapper;

    @Override
    public List<AuthRoleElementOperation> getRoleElementOperations(Set<Long> roleIdSet) {

        return authRoleElementOperationMapper.getRoleElementOperations(roleIdSet);
    }
}




