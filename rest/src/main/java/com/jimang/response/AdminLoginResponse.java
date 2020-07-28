package com.jimang.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/25
 * @Version 1.0
 */
@Data
public class AdminLoginResponse {
    @JsonProperty("access_token")
    private String accessToken;
    private String role;

}
