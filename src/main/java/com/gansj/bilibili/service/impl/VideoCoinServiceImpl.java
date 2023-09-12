package com.gansj.bilibili.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.dao.VideoCoin;
import com.gansj.bilibili.service.VideoCoinService;
import com.gansj.bilibili.mapper.VideoCoinMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author GSJ
* @description 针对表【video_coin(视频投币记录表)】的数据库操作Service实现
* @createDate 2023-08-06 15:42:42
*/
@Service
public class VideoCoinServiceImpl extends ServiceImpl<VideoCoinMapper, VideoCoin>
    implements VideoCoinService{

    @Resource
    private VideoCoinMapper videoCoinMapper;

    @Override
    public void updateVideoCoin(VideoCoin videoCoin) {
        videoCoinMapper.updateVideoCoin(videoCoin);
    }

    @Override
    public Long getVideoCoinsAmount(Long videoId) {

        return videoCoinMapper.getVideoCoinsCount(videoId);
    }
}




