package com.tt.sns.model.huawei;

import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/12
 * @Version 1.0
 */
@Data
public class HuaWeiPushMessage {
    /**
     * 自定义消息负载。样例："your data"或者key-value形式 "{'param1':'value1','param2':'value2'}"。
     消息体中有message.data，没有message.notification和message.android.notification，消息类型为透传消息。
     如果用户发送的是网页应用的透传消息，那么接收消息中字段orignData为透传消息内容。
     */
    private String data;
    /**
     * 通知
     */
    private Notification notification;
    /**
     * 安卓
     */
    private AndroidConfig android;
    /**
     * ios
     */
    private ApnsConfig apns;




}
