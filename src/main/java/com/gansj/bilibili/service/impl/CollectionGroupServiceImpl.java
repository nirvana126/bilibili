package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.CollectionGroup;
import com.gansj.bilibili.service.CollectionGroupService;
import com.gansj.bilibili.mapper.CollectionGroupMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【collection_group(用户收藏分组表)】的数据库操作Service实现
* @createDate 2023-08-06 15:23:00
*/
@Service
public class CollectionGroupServiceImpl extends ServiceImpl<CollectionGroupMapper, CollectionGroup>
    implements CollectionGroupService{

}




