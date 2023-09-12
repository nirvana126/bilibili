package com.gansj.bilibili.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gansj.bilibili.constant.RedisKeyConstant;
import com.gansj.bilibili.constant.RocketmqConstant;
import com.gansj.bilibili.dao.UserMoment;
import com.gansj.bilibili.service.UserMomentService;
import com.gansj.bilibili.mapper.UserMomentMapper;
import com.gansj.bilibili.utils.RocketMQUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
* @author GSJ
* @description 针对表【user_moment(用户动态表)】的数据库操作Service实现
* @createDate 2023-07-24 15:47:10
*/
@Service
public class UserMomentServiceImpl extends ServiceImpl<UserMomentMapper, UserMoment>
    implements UserMomentService{

    @Resource
    private ApplicationContext applicationContext;

    @Resource
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void addUserMoment(UserMoment userMoment) throws Exception{
        userMoment.setId(null);
        userMoment.setCreateTime(new Date());
        this.save(userMoment);
        DefaultMQProducer producer = (DefaultMQProducer) applicationContext.getBean("momentsProducer");
        Message message = new Message(RocketmqConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoment).getBytes(StandardCharsets.UTF_8));
        RocketMQUtil.syncSendMsg(producer,message);
    }

    @Override
    public List<UserMoment> getUserSubscribedMoments(Long userId) {

        String key = RedisKeyConstant.USER_MOMENT_SUBSCRIBED_KEY_PREFIX+userId;
        String momentsStr = redisTemplate.opsForValue().get(key);
        List<UserMoment> userMoments = JSONObject.parseArray(momentsStr, UserMoment.class);
        return userMoments;
    }
}




