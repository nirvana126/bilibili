package com.gansj.bilibili.controller;

import com.gansj.bilibili.common.BaseResponse;
import com.gansj.bilibili.common.ResultUtils;
import com.gansj.bilibili.dao.Danmu;
import com.gansj.bilibili.manager.UserSupport;
import com.gansj.bilibili.service.DanmuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@RestController
public class DanmuController {


    @Resource
    private UserSupport userSupport;

    @Resource
    private DanmuService danmuService;

    @GetMapping("/danmu-list")
    @ApiOperation(value = "查询指定时间段的弹幕")
    public BaseResponse<List<Danmu>> getDanmuList(@RequestParam Long videoId, String startTime, String endTime) throws Exception {
        List<Danmu> list;
        //先判断用户是否登录
        try {
            userSupport.getCurrentUserId();
            list = danmuService.getDanmuList(videoId,startTime,endTime);
        } catch (Exception ignored) {
            list = danmuService.getDanmuList(videoId,null,null);
        }
        return ResultUtils.success(list);
    }
}
