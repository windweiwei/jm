package com.jimang.services;

import com.jimang.model.Device;
import com.jimang.response.BaseListResponse;
import com.jimang.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther:wind
 * @Date:2020/7/27
 * @Version 1.0
 */
public interface DeviceService {
    /**
     * 获取设备名称
     *
     * @param request request
     * @return 。。
     */
  BaseListResponse<Device> getDevices(HttpServletRequest request);

  BaseResponse deviceUpdate();

}
