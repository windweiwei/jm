package com.tt.sns.service.serviceimpl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import com.tt.sns.service.Push;
import lombok.extern.slf4j.Slf4j;

import com.tt.sns.model.PushConfig;
import com.tt.sns.model.PushMessage;
import com.tt.sns.result.PushResult;

/**
 * Created by wind on 2020/7/7.
 */
@Slf4j
public class JiguangPush implements Push {
    private final PushConfig config;
    private static JiguangPush jiguangPush;

    private JiguangPush(PushConfig config) {
        this.config = config;
    }

    public static JiguangPush getInstance(PushConfig config) {
        if (jiguangPush == null) {
            synchronized (JiguangPush.class) {
                if (jiguangPush == null) {
                    jiguangPush = new JiguangPush(config);
                }
            }
        }
        return jiguangPush;
    }

    @Override
    public PushResult push(String token, PushMessage message, String appVersion) {
        PushResult  res =new PushResult();

        JPushClient jpushClient = new JPushClient(config.getApiSecret(), config.getApiKey(),
                null, ClientConfig.getInstance());

        // For push, all you need do is to build PushPayload object.
        PushPayload payload = buildPushObject_ios_audienceMore_messageWithExtras(message,token);

        try {
            cn.jpush.api.push.PushResult result = jpushClient.sendPush(payload);
            log.info("Got result - " + result);

        } catch (APIConnectionException e) {
            // Connection error, should retry later
            log.error("Connection error, should retry later", e);

        } catch (APIRequestException e) {
            // Should review the error, and fix the request
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }
        try {
            cn.jpush.api.push.PushResult result = null;
            res.setSuccess(result.isResultOK());
            try {
                result = jpushClient.sendPush(payload);
            } catch (APIConnectionException e) {
                log.info("Error message: " + e.getMessage());
            } catch (APIRequestException e) {
                log.info("Error Code: " + e.getErrorCode());
            }
            log.info("Got result - " + result);
            Thread.sleep(5000);
            // 请求结束后，调用 NettyHttpClient 中的 close 方法，否则进程不会退出。
            jpushClient.close();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 文档
     *
     * @return
     */
    public static PushPayload buildPushObject_ios_audienceMore_messageWithExtras(PushMessage message,String token) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag(token))
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .setMessage(Message.newBuilder()
                        .setTitle(message.getTitle())
                        .setMsgContent(message.getMessage())
                        .setContentType("application/json")
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }
}
