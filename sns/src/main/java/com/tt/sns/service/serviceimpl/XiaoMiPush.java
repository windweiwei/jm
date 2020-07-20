package com.tt.sns.service.serviceimpl;


import com.tt.sns.service.Push;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import lombok.extern.slf4j.Slf4j;

import com.tt.sns.model.PushConfig;
import com.tt.sns.model.PushMessage;
import com.tt.sns.result.PushResult;
import com.tt.sns.util.VersionUtil;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 小米推送参考资料：https://dev.mi.com/mipush/docs/server-sdk/message/
 *
 * @Author: wind
 * @Date: 2020/7/5
 */
@Slf4j
public class XiaoMiPush implements Push {
    

    static {
        Constants.useOfficial();
    }

    private final PushConfig config;

    private static XiaoMiPush xiaoMiPush;

    private static final AtomicInteger NOTIFY_ID = new AtomicInteger(0);

    private XiaoMiPush(PushConfig config) {
        this.config = config;
    }

    public static XiaoMiPush getInstance(PushConfig config) {
        if (xiaoMiPush == null) {
            synchronized (JiguangPush.class) {
                if (xiaoMiPush == null) {
                    xiaoMiPush = new XiaoMiPush(config);
                }
            }
        }
        return xiaoMiPush;
    }



    @Override
    public PushResult push(String token, PushMessage message, String appVersion) {
        PushResult result = new PushResult();
        Sender sender = new Sender(config.getApiSecret());
        String messagePayload = message.getMessage();
        String title = message.getTitle();
        String description = message.getMessage();
        Integer notifyId = NOTIFY_ID.getAndIncrement();
        //负数时通知栏只显示1条消息
        if (notifyId.intValue() < 0) {
            NOTIFY_ID.set(0);
        }
        //构建消息
        Message.Builder builder = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(config.getPackname())
                .extra("type", message.getDevType())
                .notifyId(notifyId)
                .notifyType(-1);     // 使用默认提示音提示 + 震动 + 呼吸灯

        //app 2.0版本后，点击跳转由APP端控制
        if (!VersionUtil.isNewVersion(appVersion)) {
            //预定义通知栏通知的点击行为 --》改为自定义消息通知
            builder.extra(Constants.EXTRA_PARAM_NOTIFY_FOREGROUND, "1");
        } else {
            builder.extra(Constants.EXTRA_PARAM_NOTIFY_FOREGROUND, "1");
        }
        String deviceType = "";

        builder.extra("deviceType", "");

        try {
            Result rs = sender.send(builder.build(), token, 3);
            //小米消息发送成功, 服务器返回消息ID, 否则返回Null
            if (rs.getMessageId() != null && rs.getErrorCode().getValue() == ErrorCode.Success.getValue()) {
                log.info("xiaomi push success messageId: {}, errorReason: {}，token: {}, deviceType: {}",
                        rs.getMessageId(), rs.getReason(), token, deviceType);
                result.setSuccess(true);
            } else {
                log.error("xiaomi push error messageId: {}, errorCode: {},errorReason: {}, token: {}, "
                                + "deviceType: {}", rs.getMessageId(), rs.getErrorCode().getValue(), rs.getReason(),
                        token, deviceType);
                result.setSuccess(false);
                if (rs.getErrorCode().getValue() == ErrorCode.InvalidUser.getValue()) {
                    result.setInvalidToken(true);
                }
            }
        } catch (Exception e) {
            log.error("xiaomi push error token：{}， error：{}", token, e.getMessage());
            result.setSuccess(false);
        }
        return result;
    }


}
