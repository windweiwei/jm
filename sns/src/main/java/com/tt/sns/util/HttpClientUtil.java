package com.tt.sns.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tt.sns.request.HttpRequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @Auther:wind
 * @Date:2020/7/11
 * @Version 1.0
 */
@Component
public class HttpClientUtil {
    @Autowired
    private RestTemplate restTemplate;

    public JsonObject post(HttpRequestParam requestParam, String uri) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        // 添加额外http请求头参数
        headers.setContentType(type);
        Gson gson = new Gson();
        gson.toJson(requestParam.getParam());
        HttpEntity request = new HttpEntity(gson, headers);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        for (String s : requestParam.getHeaderMap().keySet()) {
            headers.add(s, requestParam.getHeaderMap().get(s));
        }
        return restTemplate.postForObject(uri, request, JsonObject.class);
    }
}
