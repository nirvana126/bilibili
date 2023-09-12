package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.FollowingGroup;
import com.gansj.bilibili.service.FollowingGroupService;
import com.gansj.bilibili.mapper.FollowingGroupMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【following_group(用户关注分组表)】的数据库操作Service实现
* @createDate 2023-07-23 11:18:12
*/
@Service
public class FollowingGroupServiceImpl extends ServiceImpl<FollowingGroupMapper, FollowingGroup>
    implements FollowingGroupService{

}




