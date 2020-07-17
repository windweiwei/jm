package com.tt.sns.model.huawei;

import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/12
 * @Version 1.0
 */
@Data
public class Iospayload {
    /**
     * 借用message 和 title
     */
    private Notification alert;
}
