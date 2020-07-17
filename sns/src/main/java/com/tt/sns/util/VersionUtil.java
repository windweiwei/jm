package com.tt.sns.util;

/**
 * @Author: wind
 * @Date: 2020/7/5
 */
public class VersionUtil {
    /**
     * 判断版本号是否大于2
     * @param appVersion
     * @return
     */
    public static boolean isNewVersion(String appVersion) {
        if (appVersion == null || appVersion.isEmpty()) {
            return false;
        }
        String bigVersion = appVersion.split("\\.")[0];
        if (bigVersion == null || bigVersion.isEmpty()) {
            return false;
        }

        if (Integer.valueOf(bigVersion) >= 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否是华为新版本推送，新旧版本不兼容
     * @param appVersion
     * @return
     */
    public static boolean isHuaWeiNewVersion(String appVersion) {
        // TODO: 2020/6/12 待 APP 升级 SDK，约定相应版本号
        return false;
    }

}
