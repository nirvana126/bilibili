package com.gansj.bilibili.service;

import com.gansj.bilibili.dao.VideoCoin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author GSJ
* @description 针对表【video_coin(视频投币记录表)】的数据库操作Service
* @createDate 2023-08-06 15:42:42
*/
public interface VideoCoinService extends IService<VideoCoin> {

    void updateVideoCoin(VideoCoin videoCoin);

    Long getVideoCoinsAmount(Long videoId);
}
