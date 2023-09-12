package com.gansj.bilibili.mapper;

import com.gansj.bilibili.dao.VideoCoin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author GSJ
* @description 针对表【video_coin(视频投币记录表)】的数据库操作Mapper
* @createDate 2023-08-06 15:42:42
* @Entity com.gansj.bilibili.dao.VideoCoin
*/
public interface VideoCoinMapper extends BaseMapper<VideoCoin> {

    void updateVideoCoin(VideoCoin videoCoin);

    Long getVideoCoinsCount(Long videoId);
}




