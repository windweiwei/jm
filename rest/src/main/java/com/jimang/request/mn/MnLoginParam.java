package com.jimang.request.mn;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fashionbrot.validated.annotation.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/21
 * @Version 1.0
 */
@Data
public class MnLoginParam {

    private String username;

    private String password;
    @JsonProperty("app_key")
    private String appKey;

    @JsonProperty("app_secret")
    private String appSecret;

    @JsonProperty("app_type")
    private String appType;
}
