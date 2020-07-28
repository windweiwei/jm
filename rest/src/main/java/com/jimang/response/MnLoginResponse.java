package com.jimang.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/21
 * @Version 1.0
 */
@Data
public class MnLoginResponse {
    private Integer code;
    private String msg;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("idm_token")
    private String idmToken;
    @JsonProperty("user_id")
    private String userId;
}
