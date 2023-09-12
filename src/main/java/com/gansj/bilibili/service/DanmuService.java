package com.gansj.bilibili.service;

import com.gansj.bilibili.dao.Danmu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.text.ParseException;
import java.util.List;

/**
* @author GSJ
* @description 针对表【danmu(弹幕表)】的数据库操作Service
* @createDate 2023-08-08 21:37:35
*/
public interface DanmuService extends IService<Danmu> {

    void addDanmu(Danmu danmu);

    void saveDanmuTuRedis(Danmu danmu);

    void asyncAddDanmu(Danmu danmu);

    List<Danmu> getDanmuList(Long videoId, String startTime, String endTime) throws ParseException;
}
