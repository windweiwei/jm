package com.tt.sns.model;

import lombok.Data;

/**
 * @Author: wind
 * @Date: 2020/7/5
 */
@Data
public class PushMessage {
    /**
     * 标题
     */
    private String title;
    /**
     * 推送内容
     */
    private String message;
}
