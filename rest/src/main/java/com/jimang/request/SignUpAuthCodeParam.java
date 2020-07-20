package com.jimang.request;

import com.github.fashionbrot.validated.annotation.Email;
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
@ApiModel(value = "注册时选择其一发送验证码")
public class SignUpAuthCodeParam {
    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "邮件")
    private String email;
}
