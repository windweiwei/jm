package com.tt.sns.model.huawei;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @Auther:wind
 * @Date:2020/7/12
 * @Version 1.0
 */
@Data
public class HmsOptions {
    /**
     * 目标用户类型，取值如下：
     * 1：测试用户
     * 2：正式用户
     * 3：VoIP用户
     */
    @JsonProperty("target_user_type")
    private Integer targetUserType;
}
