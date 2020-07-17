package com.tt.sns.model.huawei;

import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/12
 * @Version 1.0
 */
@Data
public class AndroidConfig {
    /**
     * 用户设备离线时，Push服务器对离线消息缓存机制的控制方式，用户设备上线后缓存消息会再次下发，取值如下：
     * 0：对每个应用发送到该用户设备的离线消息只会缓存最新的一条；
     * -1：对所有离线消息都缓存
     * 1~100：离线消息缓存分组标识，对离线消息进行分组缓存，每个应用每一组最多缓存一条离线消息；
     * 如果开发者发送了10条消息，其中前5条的collapse_key为1，后5条的collapse_key为2，那么待用户上线后collapse_key为1和2的分别下发最新的一条消息给最终用户
     */
    private Integer collapseKey;
    /**
     * 透传消息投递优先级，取值如下：
     * HIGH
     * NORMAL
     */
    private String urgency;
    /**
     * 标识高优先级透传消息的特殊场景，仅取值如下：
     * PLAY_VOICE：语音播报
     * VOIP：VoIP电话
     * ALARM：告警
     */
    private String category;
    private static AndroidConfig androidConfig;

    private AndroidConfig() {
        collapseKey = 100;
        urgency = "NORMAL";
    }
    public static AndroidConfig getInstance() {
        if (androidConfig == null) {
            synchronized (AndroidConfig.class) {
                if (androidConfig == null) {
                    androidConfig = new AndroidConfig();
                }
            }
        }
        return androidConfig;
    }


}
