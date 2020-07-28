package com.jimang.controller;

import com.github.fashionbrot.validated.annotation.Validated;
import com.jimang.model.Device;
import com.jimang.response.BaseListResponse;
import com.jimang.response.BaseResponse;
import com.jimang.services.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

}
