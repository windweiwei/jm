package com.tt.sns.service.serviceimpl;

import com.google.gson.JsonObject;

import com.tt.sns.model.huawei.*;
import com.tt.sns.request.HttpRequestParam;
import com.tt.sns.service.Push;
import com.tt.sns.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.tt.sns.model.PushConfig;
import com.tt.sns.model.PushMessage;
import com.tt.sns.result.PushResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wind on 2020/7/10.
 */
public class HuaweiPush implements Push {
    @Autowired
    private HttpClientUtil httpClientUtil;

    /**
     * HTTPS下行消息
     */
    private final String url = "https://push-api.cloud.huawei.com/v1/[appid]/messages:send";

    private String realUrl;


    private PushConfig config;
    private static HuaweiPush huaweiPush;

    private HuaweiPush(PushConfig config) {
        this.config = config;
        realUrl = url.replace("[appid]", config.getApiKey());
    }

    public static HuaweiPush getInstance(PushConfig config) {
        if (huaweiPush == null) {
            synchronized (JiguangPush.class) {
                if (huaweiPush == null) {
                    huaweiPush = new HuaweiPush(config);
                }
            }
        }
        return huaweiPush;
    }

    @Override
    public PushResult push(String token, PushMessage message, String appVersion) {
        PushResult result = new PushResult();
        //oauth 2。0 获取token
        String authToken = null;
        HttpRequestParam httpRequestParam = new HttpRequestParam();
        HuaweiHttpBody body = getBody(message);
        httpRequestParam.setParam(body);
        Map<String, String> headMap = new HashMap<>();
        headMap.put("Authorization", authToken);
        JsonObject jsonObject = httpClientUtil.post(httpRequestParam, url);
        if (jsonObject.get("code").equals(200)) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 装param
     *
     * @param message 。。
     */
    private HuaweiHttpBody getBody(PushMessage message) {
        //安卓
        HuaWeiPushMessage huaWeiPushMessage = new HuaWeiPushMessage();
        //这里可以单例
        Notification notification = new Notification();
        notification.setTitle(message.getTitle());
        notification.setImage(message.getMessage());
        AndroidConfig androidConfig = AndroidConfig.getInstance();
        huaWeiPushMessage.setAndroid(androidConfig);
        ApnsConfig apnsConfig = new ApnsConfig();
        HmsOptions hmsOptions = new HmsOptions();
        hmsOptions.setTargetUserType(2);
        apnsConfig.setHmsOptions(hmsOptions);
        Iospayload iospayload = new Iospayload();
        iospayload.setAlert(notification);
        apnsConfig.setPayload(iospayload);
        huaWeiPushMessage.setApns(apnsConfig);
        huaWeiPushMessage.setNotification(notification);
//        huaWeiPushMessage.setData();自定义消息
        HuaweiHttpBody body = new HuaweiHttpBody();
        body.setMessage(huaWeiPushMessage);
        body.setValidateOnly(true);
        return body;

    }
}
