package com.tt.sns.service.serviceimpl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.tt.sns.result.PushResult;
import com.tt.sns.service.Push;
import lombok.extern.slf4j.Slf4j;

import com.tt.sns.model.PushConfig;
import com.tt.sns.model.PushMessage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * FCM官方参考资料：https://firebase.google.cn/docs/admin/setup?hl=zh-cn#initialize_the_sdk
 *
 * @Author: wind
 * @Date: 2020/7/5
 */
@Slf4j
public class FCMPush implements Push {
    private FirebaseApp firebaseApp = null;
    private final PushConfig config;
    private static FCMPush fcmPush;

    public static FCMPush getInstance(PushConfig config) {
        if (fcmPush == null) {
            synchronized (JiguangPush.class) {
                if (fcmPush == null) {
                    fcmPush = new FCMPush(config);
                }
            }
        }
        return fcmPush;
    }

    private FCMPush(PushConfig config) {
        this.config = config;
        InputStream serviceAccount = null;
        try {
            if (config.getApiKey().contains("classpath:")) {
                serviceAccount = FCMPush.class.getResourceAsStream("/" + config.getApiKey().split(":")[1]);
            } else {
                serviceAccount = FCMPush.class.getResourceAsStream("/" + config.getApiKey());
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(config.getApiSecret())
                    .setReadTimeout(10000)
                    .setConnectTimeout(10000)
                    .build();
            //如果该name对应的实例已存在则应先删除，然后再创建。若不同步则存在并发问题
            synchronized (FCMPush.class) {
                List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
                for (FirebaseApp item : firebaseApps) {
                    if (item.getName().equals(config.getFirmId().toString())) {
                        item.delete();
                        break;
                    }
                }
                //设置FirebaseApp name，防止name冲突
                firebaseApp = FirebaseApp.initializeApp(options, config.getFirmId().toString());
            }
            log.info("初始化Admin Java SDK成功");
        } catch (FileNotFoundException e) {
            log.error("初始化Admin Java SDK失败！未找到服务帐号凭据JSON文件！" + e.getMessage());
        } catch (IOException e) {
            log.error("初始化Admin Java SDK失败！读取服务帐号凭据JSON文件失败！" + e.getMessage());
        } catch (Exception e) {
            log.error("FCM初始化Admin Java SDK失败！" + e.getMessage());
        } finally {
            if (serviceAccount != null) {
                try {
                    serviceAccount.close();
                } catch (IOException e) {
                    log.error("FCM配置项文件IO流关闭异常! " + e.getMessage());
                }
            }
        }
    }

    @Override
    public PushResult push(String token, PushMessage message, String appVersion) {
        PushResult result = new PushResult();

        Map<String, String> data = new HashMap<>();

        Message msg = Message.builder()
                .setAndroidConfig(AndroidConfig.builder()
                        // 1 hour in milliseconds
                        .setTtl(3600 * 1000)
                        .setPriority(AndroidConfig.Priority.NORMAL) //消息优先级
                        .setRestrictedPackageName(config.getPackname()) //应用的软件包名称
                        .putAllData(data)
                        .setNotification(AndroidNotification.builder()
                                .setTitle(message.getTitle())
                                .setBody(message.getMessage())
                                .setIcon("stock_ticker_update")
                                .setColor("#f45342")
                                .build())
                        .build())
                .setToken(token)
                .build();
        // Send a message to the device corresponding to the provided
        // registration token.
        String response = "";

        try {
            log.info("FCM开始发送消息！message: " + "");
            response = FirebaseMessaging.getInstance(firebaseApp).send(msg);
        } catch (FirebaseMessagingException e) {
            log.error("FCM发送消息失败！" + "error_code: " + e.getErrorCode() + ", message: "
                    + e.getMessage() + " token:" + token);
            log.error(String.valueOf(e.getCause()));
            result.setSuccess(false);
            //判断是否token失效
            String errorCode = e.getErrorCode();
            if (errorCode.equals("invalid-registration-token")
                    || errorCode.equals("registration-token-not-registered")) {
                result.setInvalidToken(true);
            }
            return result;
        }
        // Response is a message ID string.
        log.info("FCM Successfully sent message: " + response);
        result.setSuccess(true);
        return result;
    }
}
