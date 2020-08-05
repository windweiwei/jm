package com.jimang.services;

import com.jimang.model.Device;
import com.jimang.model.SnDeviceInfo;
import com.jimang.request.DeviceBindToUserParam;
import com.jimang.request.DeviceUpdateParam;
import com.jimang.request.SingleDeviceParam;
import com.jimang.response.BaseListResponse;
import com.jimang.response.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    /**
     * 变更设备信息
     *
     * @param param   param
     * @param request request
     * @return 。。
     */
    BaseResponse deviceUpdate(DeviceUpdateParam param, HttpServletRequest request);

    /**
     * 设备绑定
     *
     * @param param   param
     * @param request request
     * @return ..
     */
    BaseResponse deviceBindToUser(DeviceBindToUserParam param, HttpServletRequest request);

    /**
     * 设备解绑
     *
     * @param param   param
     * @param request request
     * @return ..
     */
    BaseResponse deviceUnbind(DeviceBindToUserParam param, HttpServletRequest request);

    /**
     * 根据sn 获取设备信息
     *
     * @param param   param
     * @param request 。。
     * @return 。。
     */
    BaseResponse<SnDeviceInfo> getDeviceInfoFormSn(SingleDeviceParam param, HttpServletRequest request);

    /**
     *
     * @param file file
     * @param request request
     * @return ..
     */
    BaseResponse uploadDeviceImg(MultipartFile file,String sn,Integer appType,HttpServletRequest request);

    /**设备封面下载
     *
     * @param md5 sn
     * @param request request
     */

    void downloadDeviceImg(String md5, String sn,
                           HttpServletRequest request, HttpServletResponse response);


}
