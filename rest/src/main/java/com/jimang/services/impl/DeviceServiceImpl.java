package com.jimang.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimang.enums.ResponseEnum;
import com.jimang.intercepter.AcessTokenIntercepter;
import com.jimang.mapper.AppMapper;
import com.jimang.mapper.DevicesMapper;
import com.jimang.model.App;
import com.jimang.model.Device;
import com.jimang.model.Devices;
import com.jimang.response.BaseListResponse;
import com.jimang.services.DeviceService;
import com.jimang.util.DeviceUtil;
import com.jimang.util.RedisUtil;
import com.jimang.util.ResponseUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Auther:wind
 * @Date:2020/7/27
 * @Version 1.0
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DevicesMapper, Devices> implements DeviceService {
    @Autowired
    private DeviceUtil deviceUtil;
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public BaseListResponse<Device> getDevices(HttpServletRequest request) {
        BaseListResponse<Device> response = new BaseListResponse<>();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        List<Device> deviceList = new ArrayList<>();
        //mn 等app
        QueryWrapper<App> appQueryWrapper = new QueryWrapper<>();
        List<App> apps = appMapper.selectList(appQueryWrapper);
        for (App app : apps) {
            Set<Object> accessTokens = redisUtil.sGet("mn" + request.getHeader("access_token"));
            if (accessTokens != null) {
                for (Object accessToken : accessTokens) {
                    List<Device> devices = deviceUtil.getDevices(app, String.valueOf(accessToken));
                    deviceList.addAll(devices);
                }
            }

        }
        QueryWrapper<Devices> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<Devices> devices = list(queryWrapper);
        for (Devices device : devices) {
            Device findCamDevice = new Device();
            BeanUtils.copyProperties(device, findCamDevice);
            deviceList.add(findCamDevice);
        }
        response.setList(deviceList);
        response.setCount(deviceList.size());
        ResponseUtil.getListBaseResponse(response, ResponseEnum.SUCCESS);
        return response;
    }
}
