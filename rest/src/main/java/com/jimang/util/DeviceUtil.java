package com.jimang.util;

import com.jimang.model.App;
import com.jimang.model.Device;
import com.jimang.model.HttpRequestParam;
import com.jimang.request.mn.MnDeviceBindParam;
import com.jimang.request.mn.MnDeviceUpdateParam;
import com.jimang.response.BaseResponse;
import com.jimang.response.MnDeviceInfoForSn;
import com.jimang.response.MnDevicesResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

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

    public String mnBaseUrl = "http://restts.bullyun.com";

    public static final String GET_DEVICE_URL = "/api/v3/devices/sort/list";

    public static final String BIND_DEVICE_URL = "/api/v2/devices/{sn}/bind";

    public static final String UPDATE_DEVICE_URL = "/api/v1/devices/{sn}/modify";

    public static final String DEVICE_UNBIND_URL = "/api/v1/devices/{sn}/unbind";

    public static final String DEVICE_INFO_URL = "/api/v3/device/info/sn";

    public static final String DEVICE_UPLOAD_URL = "/api/v1/devices/{sn}/logo/upload";


    public List<Device> getDevices(App app, String accessToken) {
        HttpRequestParam param = new HttpRequestParam();
        Map<String, String> heads = new HashMap<>();
        heads.put("app_key", app.getAppKey());
        heads.put("app_secret", app.getAppSecret());
        heads.put("access_token", accessToken);
        HttpRequestParam httpRequestParam = new HttpRequestParam();
        httpRequestParam.setHeaderMap(heads);
        httpRequestParam.setParam(new Object());
        MnDevicesResponse response = httpClientUtil.post(httpRequestParam,
                mnBaseUrl + GET_DEVICE_URL, MnDevicesResponse.class);
        if (response.getCode().equals(2000)) {
            return response.getDevices();
        }
        return null;
    }

    /**
     * 设备绑定
     *
     * @param app         app
     * @param sn          设备sn
     * @param vn          vn
     * @param accessToken access——token
     * @return 。。
     */
    public BaseResponse bindDevice(App app, String sn, String vn, String accessToken) {
        HttpRequestParam param = new HttpRequestParam();
        Map<String, String> headers = new HashMap<>();
        param.setHeaderMap(headers);
        MnDeviceBindParam deviceBindParam = new MnDeviceBindParam();
        BeanUtils.copyProperties(app, deviceBindParam);
        deviceBindParam.setAccessToken(accessToken);
        deviceBindParam.setSn(sn);
        deviceBindParam.setVn(vn);
        param.setParam(deviceBindParam);
        String url = BIND_DEVICE_URL.replace("{sn}", sn);
        return httpClientUtil.post(param, mnBaseUrl + url, BaseResponse.class);

    }

    /**
     * 更新设备
     *
     * @param app     app
     * @param sn      设备sn
     * @param devName devName
     * @return 。。
     */
    public BaseResponse updateDevice(App app, String sn, String devName, String accessToken) {
        HttpRequestParam param = new HttpRequestParam();
        Map<String, String> headers = new HashMap<>();
        param.setHeaderMap(headers);
        MnDeviceUpdateParam mnDeviceUpdateParam = new MnDeviceUpdateParam();
        BeanUtils.copyProperties(app, mnDeviceUpdateParam);
        mnDeviceUpdateParam.setAppkey(app.getAppKey());
        mnDeviceUpdateParam.setAppSecret(app.getAppSecret());
        mnDeviceUpdateParam.setAccessToken(accessToken);
        mnDeviceUpdateParam.setSn(sn);
        mnDeviceUpdateParam.setDevName(devName);
        param.setParam(mnDeviceUpdateParam);
        String url = UPDATE_DEVICE_URL.replace("{sn}", sn);
        return httpClientUtil.post(param, mnBaseUrl + url, BaseResponse.class);
    }

    /**
     * 解绑设备
     *
     * @param app         app
     * @param sn          sn
     * @param accessToken accessToken
     * @return 。。
     */
    public BaseResponse unbindDevice(App app, String sn, String accessToken) {
        HttpRequestParam param = new HttpRequestParam();
        MnDeviceBindParam deviceBindParam = new MnDeviceBindParam();
        Map<String, String> headers = new HashMap<>();
        param.setHeaderMap(headers);
        BeanUtils.copyProperties(app, deviceBindParam);
        deviceBindParam.setAccessToken(accessToken);
        deviceBindParam.setSn(sn);
        param.setParam(deviceBindParam);
        String url = DEVICE_UNBIND_URL.replace("{sn}", sn);
        return httpClientUtil.post(param, mnBaseUrl + url, BaseResponse.class);
    }

    /**
     * 根据sn 获取sn信息
     *
     * @param app         app
     * @param sn          sn
     * @param accessToken accessToken
     * @return 。。
     */
    public MnDeviceInfoForSn getDeviceInfoFormSn(App app, String sn, String accessToken) {
        HttpRequestParam param = new HttpRequestParam();
        MnDeviceBindParam deviceBindParam = new MnDeviceBindParam();
        deviceBindParam.setSn(sn.trim());
        Map<String, String> headers = new HashMap<>();
        headers.put("app_key", app.getAppKey());
        headers.put("app_secret", app.getAppSecret());
        headers.put("access_token", accessToken);
        param.setParam(deviceBindParam);
        param.setHeaderMap(headers);
        return httpClientUtil.post(param, mnBaseUrl + DEVICE_INFO_URL, MnDeviceInfoForSn.class);
    }

    /**
     * 上传设备图片
     *
     * @param app         app
     * @param sn          sn
     * @param accessToken accessToken
     * @param file        file
     */
    public Boolean uploadDeviceImg(App app, String sn, String accessToken, MultipartFile file) {
        String url = mnBaseUrl + DEVICE_UPLOAD_URL.replace("{sn}", sn);
        String result = httpClientUtil.sendRequest(url, app, accessToken, file);
        return result.contains("2000");
    }


}
