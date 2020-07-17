package com.tt.sns.request;

import lombok.Data;

import java.util.Map;

/**
 * @Auther:wind
 * @Date:2020/7/11
 * @Version 1.0
 */
@Data
public class HttpRequestParam {
    /**
     * 参数
     */
    private Object param;
    /**
     * 头信息
     */
    private Map<String, String> headerMap;

}
