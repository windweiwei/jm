package com.jimang.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.fashionbrot.validated.annotation.Email;
import com.github.fashionbrot.validated.annotation.NotEmpty;
import com.github.fashionbrot.validated.annotation.NotNull;
import com.github.fashionbrot.validated.annotation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/20
 * @Version 1.0
 */
@Data
@ApiModel
public class UserForgotPasswordUpdateParam {
    @ApiModelProperty(value = "phone")
    private String phone;

    @ApiModelProperty(value = "email")
    private String email;
    @ApiModelProperty(value = "激活码")
    @JsonProperty("active_code")
    @NotNull
    private Integer activeCode;
    @NotEmpty
    @ApiModelProperty(value = "密码")
    private String password;

}
