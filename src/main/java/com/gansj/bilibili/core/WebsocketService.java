package com.gansj.bilibili.core;

import com.alibaba.fastjson.JSONObject;
import com.gansj.bilibili.constant.RocketmqConstant;
import com.gansj.bilibili.dao.Danmu;
import com.gansj.bilibili.service.DanmuService;
import com.gansj.bilibili.utils.RocketMQUtil;
import com.gansj.bilibili.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@ServerEndpoint("/imserver/{token}")
public class WebsocketService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    public static final ConcurrentHashMap<String,WebsocketService> WEBSOCKET_MAP=new ConcurrentHashMap<>();

    private Session session;

    public Session getSession() {
        return session;
    }

    public String getSessionId() {
        return sessionId;
    }

    private String sessionId;

    private Long userId;

    private static ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(ApplicationContext applicationContext){
        WebsocketService.APPLICATION_CONTEXT=applicationContext;
    }

    @OnOpen
    public void openConnection(Session session,@PathParam("token") String token){
        try {
            this.userId= TokenUtil.verifyToken(token);
        } catch (Exception e) {
        }
        this.sessionId = session.getId();
         this.session = session;
         if (WEBSOCKET_MAP.containsKey(sessionId)){
             WEBSOCKET_MAP.remove(sessionId);
             WEBSOCKET_MAP.put(sessionId,this);
         }else {
             WEBSOCKET_MAP.put(sessionId,this);
             ONLINE_COUNT.getAndIncrement();
         }
         logger.info("用户连接成功："+sessionId+",当前在线人数为："+ONLINE_COUNT.get());
         try {
             this.sendMessage("0");
         }catch (Exception e){
             logger.error("连接异常！");
         }
    }

    public void sendMessage(String s) throws IOException {
        this.session.getBasicRemote().sendText(s);
    }

    @OnClose
    public void closeConnection(){
        if (WEBSOCKET_MAP.containsKey(sessionId)){
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("用户退出："+sessionId+",当前在线人数为："+ONLINE_COUNT.get());
    }

    @OnMessage
    public void onMessage(String message){
        logger.info("用户信息："+sessionId + "，报文：" +message);
        if (StringUtils.isNotBlank(message)){
            try {
                //群发消息
                for (Map.Entry<String, WebsocketService> entry : WEBSOCKET_MAP.entrySet()) {
                    WebsocketService websocketService = entry.getValue();
                    //使用mq之后消息的最终的发送由消费者完成
//                    if (websocketService.session.isOpen()){
//                        websocketService.sendMessage(message);
//                    }
                    DefaultMQProducer danmusProducer =(DefaultMQProducer) APPLICATION_CONTEXT.getBean("danmusProducer");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("message",message);
                    jsonObject.put("sessionId",websocketService.getSessionId());
                    Message msg = new Message(RocketmqConstant.TOPIC_DANMUS,jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
                    RocketMQUtil.asyncSendMsg(danmusProducer,msg);
                }
                if (this.userId!=null){
                    //保存弹幕到数据库
                    Danmu danmu = JSONObject.parseObject(message,Danmu.class);
                    danmu.setUserId(userId);
                    danmu.setCreateTime(new Date());
                    DanmuService danmuService = (DanmuService)APPLICATION_CONTEXT.getBean("danmuService");
                    danmuService.asyncAddDanmu(danmu);//异步存储
                    //保存弹幕到Redis
                    danmuService.saveDanmuTuRedis(danmu);
                }
            } catch (Exception e) {
                logger.error("弹幕接收出现问题！");
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable error){

    }

    //定时任务：每隔5秒统计在线人数
    @Scheduled(fixedRate = 5000)
    public void noticeOnlineCount() throws Exception{
        for (Map.Entry<String, WebsocketService> entry : WEBSOCKET_MAP.entrySet()) {
            WebsocketService websocketService = entry.getValue();
            if (websocketService.getSession().isOpen()){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("onlineCount",ONLINE_COUNT.get());
                jsonObject.put("msg","当前在线人数为："+ONLINE_COUNT.get());
                websocketService.sendMessage(jsonObject.toJSONString());
            }
        }
    }
}
