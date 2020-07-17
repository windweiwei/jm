package com.tt.sns.model.huawei;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @Auther:wind
 * @Date:2020/7/12
 * @Version 1.0
 */
@Data
public class ApnsConfig {
    /**
     * 这里Object
     */
    private String headers;

    private Iospayload payload;

    @JsonProperty("hms_options")
    private HmsOptions hmsOptions;


}
