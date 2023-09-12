package com.gansj.bilibili.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.constant.RedisKeyConstant;
import com.gansj.bilibili.dao.Danmu;
import com.gansj.bilibili.service.DanmuService;
import com.gansj.bilibili.mapper.DanmuMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* @author GSJ
* @description 针对表【danmu(弹幕表)】的数据库操作Service实现
* @createDate 2023-08-08 21:37:35
*/
@Service
public class DanmuServiceImpl extends ServiceImpl<DanmuMapper, Danmu>
    implements DanmuService{

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Resource
    private DanmuMapper danmuMapper;

    @Override
    public void addDanmu(Danmu danmu) {
        this.save(danmu);
    }

    @Override
    public void saveDanmuTuRedis(Danmu danmu) {
        String key = RedisKeyConstant.DANMU_KEY_PREFIX +danmu.getVideoId();
        String danmuStr = redisTemplate.opsForValue().get(key);
        List<Danmu> danmuList = new ArrayList<>();
        if (StringUtils.isNotBlank(danmuStr)){
            danmuList = JSONObject.parseArray(danmuStr, Danmu.class);
        }
        danmuList.add(danmu);
        redisTemplate.opsForValue().set(key,JSONObject.toJSONString(danmuList));
    }

    @Override
    @Async
    public void asyncAddDanmu(Danmu danmu) {
        this.save(danmu);
    }

    @Override
    public List<Danmu> getDanmuList(Long videoId, String startTime, String endTime) throws ParseException {
        String key = RedisKeyConstant.DANMU_KEY_PREFIX+videoId;
        String danmuStr = redisTemplate.opsForValue().get(key);
        List<Danmu> list;
        if (StringUtils.isNotBlank(danmuStr)){
            list = JSONObject.parseArray(danmuStr, Danmu.class);
            if (!StringUtils.isAnyBlank(startTime,endTime)){
                //进行日期比较，截取指定时间段的弹幕
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date startDate = dateFormat.parse(startTime);
                Date endDate = dateFormat.parse(endTime);
                List<Danmu> childList = new ArrayList<>();
                for (Danmu danmu : list) {
                    Date createTime = danmu.getCreateTime();
                    if (createTime.after(startDate) && createTime.before(endDate)){
                        childList.add(danmu);
                    }
                }
                list = childList;
            }
        }else {
            //查数据库
            Map<String ,Object> params=new HashMap<>();
            params.put("videoId",videoId);
            params.put("startTime",startTime);
            params.put("endTime",endTime);
            list=danmuMapper.getDanmus(params);
            redisTemplate.opsForValue().set(key,JSONObject.toJSONString(list));
        }
        return list;
    }
}




