package com.jimang.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/28
 * @Version 1.0
 */
@Data
public class ShareUser {

    private String id;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private String nickname;
    private String realname;
    private Integer state;
    @JsonProperty("watch_time")
    private String watchTime;
    @JsonProperty("watch_times")
    private Integer watchTimes;
    private Integer authority;
    @JsonProperty("remain_time")
    private Integer remainTime;
    @JsonProperty("device_id")
    private String deviceId;

}
