package com.jimang.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/21
 * @Version 1.0
 */
@Data
@ApiModel
public class ThirdAppLoginInfo {
    @JsonProperty("access_token")
    @ApiModelProperty(value = "第三方返回的access_token")
    private String accessToken;
    @JsonProperty("idm_token")
    @ApiModelProperty(value = "第三方返回的idm_token")
    private String idmToken;
    @JsonProperty("user_id")
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "app别名")
    @JsonProperty("app_name")
    private String appName;
    @ApiModelProperty(value = "app_key")
    @JsonProperty("app_key")
    private String appKey;
    @ApiModelProperty(value = "app_secret")
    @JsonProperty("app_secret")
    private String appSecret;
    @JsonProperty("firm_id")

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "firm_id")
    private Long firmId;
}
