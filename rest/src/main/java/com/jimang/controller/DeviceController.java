package com.jimang.controller;

import com.github.fashionbrot.validated.annotation.Validated;
import com.jimang.model.Device;
import com.jimang.model.SnDeviceInfo;
import com.jimang.request.DeviceBindToUserParam;
import com.jimang.request.DeviceUpdateParam;
import com.jimang.request.SingleDeviceParam;
import com.jimang.response.BaseListResponse;
import com.jimang.response.BaseResponse;
import com.jimang.services.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther:wind
 * @Date:2020/7/27
 * @Version 1.0
 */
@RestController
@RequestMapping("api/v1/devices")
@Api("设备模块")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @Validated
    @ApiOperation(value = "设备展示", position = 1)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseListResponse.class)})
    @GetMapping("list")
    public BaseListResponse<Device> getDevices(HttpServletRequest request) {
        return deviceService.getDevices(request);

    }

    @Validated
    @ApiOperation(value = "设备绑定", position = 2)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseListResponse.class)})
    @PostMapping("bind")
    public BaseResponse deviceBindToUser(@RequestBody DeviceBindToUserParam param, HttpServletRequest request) {
        return deviceService.deviceBindToUser(param, request);

    }

    @Validated
    @ApiOperation(value = "设备修改", position = 3)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseListResponse.class)})
    @PostMapping("update")
    public BaseResponse deviceUpdate(@RequestBody DeviceUpdateParam param, HttpServletRequest request) {
        return deviceService.deviceUpdate(param, request);
    }

    @Validated
    @ApiOperation(value = "设备解绑", position = 4)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseListResponse.class)})
    @PostMapping("unbind")
    public BaseResponse deviceUnbind(@RequestBody DeviceBindToUserParam param, HttpServletRequest request) {
        return deviceService.deviceUnbind(param, request);

    }

    @Validated
    @ApiOperation(value = "根据设备获取设备信息", position = 5)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseListResponse.class)})
    @PostMapping("info")
    public BaseResponse<SnDeviceInfo> getDeviceInfoFormSn(@RequestBody SingleDeviceParam param,
                                                          HttpServletRequest request) {

        return deviceService.getDeviceInfoFormSn(param, request);
    }


    @Validated
    @ApiOperation(value = "设备封面上传", position = 6)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseListResponse.class)})
    @PostMapping("cover/upload")
    public BaseResponse uploadDeviceImg(@RequestParam MultipartFile file,
                                        @RequestParam String sn,
                                        @RequestParam(required = false, name = "app_type") Integer appType,
                                        HttpServletRequest request) {

        return deviceService.uploadDeviceImg(file, sn, appType, request);
    }

    @Validated
    @ApiOperation(value = "设备封面下载", position = 7)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseListResponse.class)})
    @GetMapping("cover/download/{md5}/{sn}")
    public void deviceImgDownload(@PathVariable String md5,
                                  @PathVariable(value = "sn") String sn,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        deviceService.downloadDeviceImg(md5, sn, request, response);
    }




}
