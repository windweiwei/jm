package com.jimang.enums;

/**
 * Created by wind on 2020/7/9.
 */

public enum ResponseEnum {
    SUCCESS(2000, "OK"),
    INVALID_ACCESS_TOKEN(3001, "invalid access_token"),
    INVALID_PARAMS(5000, "invalid params"),
    USER_NAME_OR_PASSWORD_ERROR(5001, "user_name or password is error"),
    USER_PHONE_EXITS(5002, "user_sign_up phone exits"),
    SIGN_UP_TOKEN_INVALID(5003, "token expired or incorrect"),
    USERS_PHONE_NO_SIGN_UP(5004, "user forgot password but phone no sign up,用户忘记密码但是手机号未注册"),
    USERS_FORGOT_PASSWORD_ACTINE_CODE_ERROR(5005, "users forgot password active_code is error,验证码不对"),
    USER_PHONE_OR_EMAIL_EXITS(5006, "phone or email is not registered"),
    CONFIG_ALREADY_EXIT(6000, "config already exit");

    private Integer code;

    private String msg;

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
