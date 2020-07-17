package com.tt.sns.util;


import com.tt.sns.enums.PushTypeEnum;
import com.tt.sns.service.Push;
import com.tt.sns.service.serviceimpl.HuaweiPush;
import com.tt.sns.service.serviceimpl.JiguangPush;
import com.tt.sns.service.serviceimpl.XiaoMiPush;
import com.tt.sns.model.PushConfig;
import com.tt.sns.service.serviceimpl.FCMPush;

/**
 * Created by wind on 2020/7/10.
 */
public class PushFactory {

    /**
     * 长生push方法的简单工厂方法(单例模式)
     * 暂时这里数据库里改变了app_key或者增加了 需要重启服务。后面可以利用task 刷新数据库并重新生成
     *
     * @param pushConfig config
     * @return 。。
     */
    public static Push createPush(PushConfig pushConfig) {
        if (pushConfig.getPushType().equals(PushTypeEnum.JIGUNG.getType())) {
            return JiguangPush.getInstance(pushConfig);
        } else if (pushConfig.getPushType().equals(PushTypeEnum.HUAWEI.getType())) {
            return HuaweiPush.getInstance(pushConfig);
        } else if (pushConfig.getPushType().equals(PushTypeEnum.XIAOMI.getType())) {
            return XiaoMiPush.getInstance(pushConfig);
        } else if (pushConfig.getPushType().equals(PushTypeEnum.FCM.getType())) {
            return FCMPush.getInstance(pushConfig);
        }
        return null;
    }


}
