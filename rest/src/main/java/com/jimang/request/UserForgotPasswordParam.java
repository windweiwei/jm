package com.jimang.request;

import com.github.fashionbrot.validated.annotation.Email;
import com.github.fashionbrot.validated.annotation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/20
 * @Version 1.0
 */
@ApiModel
@Data
public class UserForgotPasswordParam {

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

}
