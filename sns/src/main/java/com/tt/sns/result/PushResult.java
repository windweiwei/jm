package com.tt.sns.result;

import lombok.Data;

/**
 * @Author: wind
 * @Date: 2020/7/5
 */
@Data
public class PushResult {
    /**
     * 推送是否成功
     */
    private boolean success;

    /**
     * token是否有效，无效的token应该删除
     */
    private boolean invalidToken;
}
