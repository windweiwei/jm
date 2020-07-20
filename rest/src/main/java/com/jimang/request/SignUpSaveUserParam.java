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
 * @Date:2020/7/19
 * @Version 1.0
 */
@Data
@ApiModel
public class SignUpSaveUserParam {
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮件")
    private String email;
    @ApiModelProperty(value = "昵称")
    @JsonProperty("nike_name")
    private String nikeName;
    @ApiModelProperty(value = "用户名")
    @JsonProperty("user_name")
    private String userName;
    @NotEmpty
    @ApiModelProperty(value = "密码")
    private String password;
    @NotEmpty
    @ApiModelProperty(value = "验证码")
    private Integer authCode;
}
