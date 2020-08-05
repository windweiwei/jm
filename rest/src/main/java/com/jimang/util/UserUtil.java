package com.jimang.util;

import com.jimang.model.App;
import com.jimang.model.HttpRequestParam;
import com.jimang.request.LoginAdminParam;
import com.jimang.request.mn.MnLoginParam;
import com.jimang.response.AdminLoginResponse;
import com.jimang.response.BaseResponse;
import com.jimang.response.CreateUserParam;
import com.jimang.response.MnLoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther:wind
 * @Date:2020/7/21
 * @Version 1.0
 */
@Component
@Slf4j
public class UserUtil {
    @Autowired
    private HttpClientUtil httpClientUtil;

    public Boolean createUser(App app, String userName, String password) {
        //admin登陆
        Map<String, String> header = new HashMap<>();
        header.put("app_key", app.getAppKey());
        header.put("app_secret", app.getAppSecret());
        HttpRequestParam param = new HttpRequestParam();
        LoginAdminParam adminParam = new LoginAdminParam();
        adminParam.setAppKey(app.getAppKey());
        adminParam.setAppSecret(app.getAppSecret());
        adminParam.setName(app.getAdminName());
        adminParam.setPassword(app.getAdminPassword());
        param.setParam(adminParam);
        param.setHeaderMap(header);
        AdminLoginResponse response = httpClientUtil
                .post(param, app.getAdminLoginApi(), AdminLoginResponse.class);
        log.info(response.getAccessToken());
        HttpRequestParam createParam = new HttpRequestParam();
        header.put("access_token", response.getAccessToken());
        createParam.setHeaderMap(header);
        CreateUserParam createUserParam = new CreateUserParam();
        createUserParam.setFirmId(String.valueOf(app.getFirmId()));
        createUserParam.setUserName("users-" + userName);
        createUserParam.setPassword(password.substring(0, 10));
        createUserParam.setUserType(1);
        createParam.setParam(createUserParam);
        BaseResponse baseResponse = httpClientUtil
                .post(createParam, app.getCreateUserApi(), BaseResponse.class);
        log.info(baseResponse.getMsg());
        return baseResponse.getCode().equals(2000);

    }

    /**
     * 发送登陆消息
     *
     * @param url       额登陆url
     * @param appKey    appKey
     * @param appSecret appSecret
     * @param userName  userName
     * @param password  password
     * @return 。。
     */
    public MnLoginResponse thirdLogin(String url, String appKey,
                                      String appSecret, String userName, String password) {

        HttpRequestParam param = new HttpRequestParam();
        Map<String, String> header = new HashMap<>();
        header.put("app_key", appKey);
        header.put("app_secret", appSecret);
        MnLoginParam loginParam = new MnLoginParam();
        loginParam.setUsername(userName);
        loginParam.setPassword(password);
        loginParam.setAppType("APP");
        loginParam.setAppKey(appKey);
        loginParam.setAppSecret(appSecret);
        param.setParam(loginParam);
        param.setHeaderMap(header);
        return httpClientUtil.post(param, url, MnLoginResponse.class);

    }


}
