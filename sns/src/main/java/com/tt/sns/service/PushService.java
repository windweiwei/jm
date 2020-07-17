package com.tt.sns.service;


import com.tt.sns.model.Alarm;
import com.tt.sns.request.PushTokenSaveParam;
import com.tt.sns.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

import com.tt.sns.model.PushConfig;

/**
 * Created by wind on 2020/7/8.
 */

public interface PushService {
    /**
     * 注册推送token
     *
     * @param param   param
     * @param request re
     * @return 。。
     */

    BaseResponse savePushToken(PushTokenSaveParam param, HttpServletRequest request);

    /**
     * 接受报警并报警
     * @param alarm  alarm
     * @param request request
     * @return 。。
     */

    BaseResponse acceptAndPush(Alarm alarm, HttpServletRequest request);

    /**
     * 推送配置
     * @param pushConfig 。。
     * @param request 。。
     * @return 。。
     */

    BaseResponse savePushConfig(PushConfig pushConfig, HttpServletRequest request);


}
