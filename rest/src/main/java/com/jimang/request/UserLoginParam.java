package com.jimang.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fashionbrot.validated.annotation.NotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/19
 * @Version 1.0
 */
@Data
@ApiModel
public class UserLoginParam {
    @NotEmpty
    @JsonProperty("user_name")
    @ApiModelProperty("用户名")
    private String userName;
    @NotEmpty
    @ApiModelProperty("密码")
    private String password;
}
