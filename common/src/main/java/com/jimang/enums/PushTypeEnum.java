package com.jimang.enums;

/**
 * Created by wind on 2020/7/10.
 */
public enum PushTypeEnum {
    /**
     * 极光推送
     */
    JIGUNG(1),
    /**
     * 华为
     */
    HUAWEI(2),
    /**
     * 小米
     */
    XIAOMI(3),
    /**
     * 谷歌
     */
    FCM(4);;
    private Integer Type;

    PushTypeEnum(Integer type) {
        Type = type;
    }

    public Integer getType() {
        return Type;
    }
}
