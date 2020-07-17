package com.tt.sns.service;


import com.tt.sns.model.PushMessage;
import com.tt.sns.result.PushResult;

/**
 * @Author: wind
 * @Date: 2020/7/5
 */
public interface Push {
    /**
     * 你是
     *
     * @param token      token
     * @param message    message
     * @param appVersion appversion
     * @return ..
     */
    PushResult push(String token, PushMessage message, String appVersion);
}
