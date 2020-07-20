package com.tt.sns.controller;


import com.tt.sns.model.Alarm;
import com.tt.sns.request.PushTokenSaveListParam;
import com.tt.sns.request.PushTokenSaveParam;
import com.tt.sns.response.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import com.tt.sns.model.PushConfig;
import com.tt.sns.service.PushService;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by wind on 2020/7/8.
 */
@Api(value = "推送模块")
@RestController
@Slf4j
public class PushController {
    @Autowired
    private PushService pushService;

    @ApiOperation(value = "推送token注册接口", notes = "当用户登陆或打开app需要注册推送token", response = BaseResponse.class)
    @PostMapping("/api/v3/push_token/save")
    public BaseResponse savePushToken(@RequestBody PushTokenSaveListParam param, HttpServletRequest request) {

        return pushService.savePushToken(param, request);
    }

    @ApiOperation(value = "消息接受并发送接口", notes = "向这个接口发送需要推送当消息")
    @PostMapping("/api/v3/alarm/push")
    public BaseResponse acceptAndPush(@RequestBody Alarm alarm, HttpServletRequest request) {
        log.info(alarm.toString());
        log.info(alarm.getUid());
        return pushService.acceptAndPush(alarm, request,null);

    }

    @PostMapping(value = "/api/v3/alarm/push",consumes = "multipart/form-data")
    public BaseResponse acceptImgAndPush(@RequestParam String uid,
                                         @RequestParam Integer type,
                                         @RequestParam Long time,
                                         @RequestParam MultipartFile file,
                                         HttpServletRequest request) {
        Alarm alarm = new Alarm();
        alarm.setUid(uid);
        alarm.setType(type);
        alarm.setTime(time);
        return pushService.acceptAndPush(alarm, request,file);
    }

    @ApiOperation(value = "推送平台相关配置注册", notes = "把平台相关当配置注册到平台")
    @PostMapping("/api/v3/push_config/save")
    public BaseResponse savePushConfig(@RequestBody PushConfig pushConfig, HttpServletRequest request) {

        return pushService.savePushConfig(pushConfig, request);

    }

    @GetMapping("/index")
    @ApiOperation(value = "测试接口", notes = "get 请求测试")
    public String getIndex() {
        return "OK";
    }

}
