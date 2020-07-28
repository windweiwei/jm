package com.jimang.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginAdminParam {
    @JsonProperty("app_key")
    private String appKey;
    @JsonProperty("app_secret")
    private String appSecret;
    private String name;
    private String password;
}
