package com.gansj.bilibili.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gansj.bilibili.dao.UserRole;

import java.util.List;

/**
* @author GSJ
* @description 针对表【user_role(用户角色关联表)】的数据库操作Mapper
* @createDate 2023-08-12 15:04:17
* @Entity generator.domain.UserRole
*/
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<UserRole> getUserRoleByUserId(Long userId);
}




