package com.jimang.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Auther:wind
 * @Date:2020/7/19
 * @Version 1.0
 */
@Data
@ApiModel
public class LoginInfo {
    @ApiModelProperty(value = "用户id")
    @JsonProperty("user_id")
    private Long userId;

    @ApiModelProperty("user_name")
    @JsonProperty("user_name")
    private String userName;

    @ApiModelProperty(value = "注册token")
    @JsonProperty("access_token")
    private String accessToken;

    @ApiModelProperty(value = "第三方登陆信息")
    @JsonProperty("login_infos")
    private List<ThirdAppLoginInfo> loginInfos;

}
