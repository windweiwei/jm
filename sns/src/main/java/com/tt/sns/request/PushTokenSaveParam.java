package com.tt.sns.request;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by wind on 2020/7/8.
 */
@Data
public class PushTokenSaveParam {
    /**
     * 注册的push——token
     */
    private String token;
    /**
     * 推送类型 1：小米 2：极光
     */
    private Integer pushType;
    /**
     * 设备id
     */
    private String uid;

    @JsonProperty("time_utc")
    private Long timeUtc;
    /**
     * 设备类型
     */
    private Integer type;

}
