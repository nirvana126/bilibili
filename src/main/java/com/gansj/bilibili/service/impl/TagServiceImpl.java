package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.Tag;
import com.gansj.bilibili.service.TagService;
import com.gansj.bilibili.mapper.TagMapper;
import org.springframework.stereotype.Service;

/**
* @author GSJ
* @description 针对表【tag(标签表)】的数据库操作Service实现
* @createDate 2023-08-05 20:10:42
*/
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
    implements TagService{

}




