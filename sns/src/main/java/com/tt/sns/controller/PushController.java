package com.tt.sns.controller;


import com.tt.sns.model.Alarm;
import com.tt.sns.request.PushTokenSaveParam;
import com.tt.sns.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import com.tt.sns.model.PushConfig;
import com.tt.sns.service.PushService;

/**
 * Created by wind on 2020/7/8.
 */
@RestController
public class PushController {
    @Autowired
    private PushService pushService;

    @PostMapping("/api/v3/push_token/save")
    public BaseResponse savePushToken(@RequestBody PushTokenSaveParam param, HttpServletRequest request) {

        return pushService.savePushToken(param, request);
    }

    @PostMapping("/api/v3/alarm/push")
    public BaseResponse acceptAndPush(@RequestBody Alarm alarm, HttpServletRequest request) {
        return pushService.acceptAndPush(alarm, request);

    }

    @PostMapping("/api/v3/push_config/save")
    public BaseResponse savePushConfig(@RequestBody PushConfig pushConfig, HttpServletRequest request) {
        return pushService.savePushConfig(pushConfig, request);

    }

    @GetMapping("/index")
    public String getIndex() {
        return "OK";
    }

}
