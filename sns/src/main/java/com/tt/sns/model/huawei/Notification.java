package com.tt.sns.model.huawei;

import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/12
 * @Version 1.0
 */
@Data
public class Notification {
    private String title;
    private String body;
    /**
     * 用户自定义的通知栏消息右侧大图标URL，如果不设置，则不展示通知栏右侧图标。URL使用的协议必须是HTTPS协议，取值样例：https://example.com/image.png。
     说明
     图标规格需满足：图标大小为40dp x 40dp，弧角大小为8dp，图标文件小于512KB。
     */
    private String image;
}
