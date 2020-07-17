package com.tt.sns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by wind on 2020/7/8.
 */
@Data
public class Alarm {
    /**
     * 标题
     */

    private String title;
    /**
     * 内容
     */

    private String msg;
    /**
     * token
     */
    private String token;
    /**
     * 设备id
     */
    private String uid;
    /**
     * 图片url
     */
    @JsonProperty("image_url")
    private String imageUrl;
    /**
     * 报警时间
     */
    private Long timeUtc;
    @JsonProperty("app_version")
    private String appVersion;
}
