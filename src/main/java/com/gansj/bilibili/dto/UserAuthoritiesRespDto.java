package com.gansj.bilibili.dto;

import com.gansj.bilibili.dao.AuthRoleElementOperation;
import com.gansj.bilibili.dao.AuthRoleMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthoritiesRespDto {

    /**
     * 元素操作相关列表
     */
    List<AuthRoleElementOperation> roleElementOperationList;

    /**
     * 页面菜单
     */
    List<AuthRoleMenu> roleMenuList;
}
