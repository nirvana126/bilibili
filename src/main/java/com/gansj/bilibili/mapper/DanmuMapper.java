package com.gansj.bilibili.mapper;

import com.gansj.bilibili.dao.Danmu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author GSJ
* @description 针对表【danmu(弹幕表)】的数据库操作Mapper
* @createDate 2023-08-08 21:37:35
* @Entity com.gansj.bilibili.dao.Danmu
*/
public interface DanmuMapper extends BaseMapper<Danmu> {

    List<Danmu> getDanmus(Map<String, Object> params);
}




