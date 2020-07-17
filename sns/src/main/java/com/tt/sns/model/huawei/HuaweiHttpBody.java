package com.tt.sns.model.huawei;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @Auther:wind
 * @Date:2020/7/12
 * @Version 1.0
 */
@Data
public class HuaweiHttpBody {
    /**
     * 控制当前是否为测试消息，测试消息只做格式合法性校验，不会推送给用户设备，取值如下：
     true：测试消息
     false：正式消息
     默认值是false。
     */
    @JsonProperty("validate_only")
    private boolean validateOnly;

    private HuaWeiPushMessage message;




}
