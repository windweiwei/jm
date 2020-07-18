package com.tt.sns.enums;

/**
 * Created by wind on 2020/7/9.
 */

public enum ResponseEnum {
    SUCCESS(2000, "OK"),
    INVALID_PARAMS(5000, "invalid params"),
    CONFIG_ALREADY_EXIT(6000, "config alredy exit");

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
