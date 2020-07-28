package com.jimang.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fashionbrot.validated.annotation.NotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/21
 * @Version 1.0
 */
@Data
@ApiModel
public class UserChangePasswordParam {

    @ApiModelProperty(value = "密码")
    @NotEmpty
    private String password;

    @ApiModelProperty(value = "验证码")
    @JsonProperty("active_code")
    @NotEmpty
    private Integer activeCode;
}
