package com.tt.sns.service;


import com.tt.sns.model.Alarm;
import com.tt.sns.request.PushTokenSaveListParam;
import com.tt.sns.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

import com.tt.sns.model.PushConfig;
import org.springframework.web.multipart.MultipartFile;

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

    BaseResponse savePushToken(PushTokenSaveListParam param, HttpServletRequest request);

    /**
     * 接受报警并报警
     * @param alarm  alarm
     * @param request request
     * @return 。。
     */

    BaseResponse acceptAndPush(Alarm alarm, HttpServletRequest request, MultipartFile file);

    /**
     * 推送配置
     * @param pushConfig 。。
     * @param request 。。
     * @return 。。
     */

    BaseResponse savePushConfig(PushConfig pushConfig, HttpServletRequest request);


}
