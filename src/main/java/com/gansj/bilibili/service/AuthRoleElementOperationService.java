package com.gansj.bilibili.service;

import com.gansj.bilibili.dao.AuthRoleElementOperation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

/**
* @author GSJ
* @description 针对表【auth_role_element_operation(权限控制--角色与元素操作关联表)】的数据库操作Service
* @createDate 2023-08-12 15:58:17
*/
public interface AuthRoleElementOperationService extends IService<AuthRoleElementOperation> {

    List<AuthRoleElementOperation> getRoleElementOperations(Set<Long> roleIdSet);
}
