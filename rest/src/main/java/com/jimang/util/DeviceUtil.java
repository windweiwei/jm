package com.jimang.util;

import com.jimang.model.App;
import com.jimang.model.Device;
import com.jimang.model.HttpRequestParam;
import com.jimang.response.MnDevicesResponse;
import com.jimang.response.MnLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:wind
 * @Date:2020/7/27
 * @Version 1.0
 */
@Component
public class DeviceUtil {
    @Autowired
    private HttpClientUtil httpClientUtil;

    public static final String GET_DEVICE_URL = "http://restts.bullyun.com/api/v3/devices/sort/list";

    public List<Device> getDevices(App app, String accessToken) {
        HttpRequestParam param = new HttpRequestParam();
        Map<String, String> heads = new HashMap<>();
        heads.put("app_key", app.getAppKey());
        heads.put("app_secret", app.getAppSecret());
        heads.put("access_token", accessToken);
        HttpRequestParam httpRequestParam = new HttpRequestParam();
        httpRequestParam.setHeaderMap(heads);
        httpRequestParam.setParam(new Object());
        MnDevicesResponse response = httpClientUtil.post(httpRequestParam, GET_DEVICE_URL, MnDevicesResponse.class);
        if (response.getCode().equals(2000)) {
            return response.getDevices();
        }
        return null;
    }


}
