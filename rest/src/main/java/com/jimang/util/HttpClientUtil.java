package com.jimang.util;


import com.jimang.model.App;
import com.jimang.model.HttpRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Auther:wind
 * @Date:2020/7/11
 * @Version 1.0
 */
@Component
@Slf4j
public class HttpClientUtil {
    @Autowired
    private RestTemplate restTemplate;

    public <T> T post(HttpRequestParam requestParam, String uri, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        // 添加额外http请求头参数
        headers.setContentType(type);
        String body = JsonMapper.writeValueAsString(requestParam.getParam());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        for (String s : requestParam.getHeaderMap().keySet()) {
            headers.add(s, requestParam.getHeaderMap().get(s));
        }
        HttpEntity request = new HttpEntity(body, headers);

        return restTemplate.postForObject(uri, request, clazz);
    }

    public String sendRequest(String url, App app, String accessToken, MultipartFile file) {
        RestTemplate restTemplate = new RestTemplate();

        //设置请求头(注意会产生中文乱码)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(
                popHeaders(app, accessToken, file), headers);
        //发送请求，设置请求返回数据格式为String
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        log.info(JsonMapper.writeValueAsString(responseEntity.getBody()));
        return JsonMapper.writeValueAsString(responseEntity.getBody());

    }

    //组装请求体
    private MultiValueMap<String, Object> popHeaders(App app, String accessToken, MultipartFile file) {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("jpg", file.getOriginalFilename());
            FileUtils.copyInputStreamToFile(file.getInputStream(), tempFile);
            Resource resource = new FileSystemResource(tempFile);
            map.add("img", resource);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            tempFile.deleteOnExit();
        }
        map.add("app_key", app.getAppKey());
        map.add("app_secret", app.getAppSecret());
        map.add("access_token", accessToken);
        return map;
    }


}
