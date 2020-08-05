package com.jimang.services.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jimang.content.OssBucketContent;
import com.jimang.enums.AppTypeEnum;
import com.jimang.enums.ResponseEnum;
import com.jimang.intercepter.AcessTokenIntercepter;
import com.jimang.intercepter.FirmKeySecretIntercepter;
import com.jimang.mapper.AppMapper;
import com.jimang.mapper.DevicesMapper;
import com.jimang.mapper.UsersMapper;
import com.jimang.model.*;
import com.jimang.request.DeviceBindToUserParam;
import com.jimang.request.DeviceUpdateParam;
import com.jimang.request.SingleDeviceParam;
import com.jimang.response.BaseListResponse;
import com.jimang.response.BaseResponse;
import com.jimang.response.MnDeviceInfoForSn;
import com.jimang.services.DeviceService;
import com.jimang.util.DeviceUtil;
import com.jimang.util.Md5Util;
import com.jimang.util.RedisUtil;
import com.jimang.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther:wind
 * @Date:2020/7/27
 * @Version 1.0
 */
@Service
@Slf4j
public class DeviceServiceImpl extends ServiceImpl<DevicesMapper, Devices> implements DeviceService {
    @Autowired
    private DeviceUtil deviceUtil;
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private OSS ossClient;

    @Override
    public BaseListResponse<Device> getDevices(HttpServletRequest request) {
        BaseListResponse<Device> response = new BaseListResponse<>();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        List<Device> deviceList = new ArrayList<>();
        //mn 等app
        QueryWrapper<App> appQueryWrapper = new QueryWrapper<>();
        List<App> apps = appMapper.selectList(appQueryWrapper);
        for (App app : apps) {
            Object accessToken = redisUtil.hget("mn" + request.getHeader("access_token"),
                    String.valueOf(app.getAppType()));
            if (accessToken != null) {
                List<Device> devices = deviceUtil.getDevices(app, String.valueOf(accessToken));
                for (Device device : devices) {
                    QueryWrapper<Devices> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("sn", device.getSn()).eq("firm_id", app.getFirmId());
                    Devices dev = getOne(queryWrapper);
                    if (dev != null) {
                        device.setCoverUrl(request.getScheme() + "://" + request.getServerName()
                                + ":" + request.getServerPort() + "/api/v1/devices/cover/download/" + dev.getLogo());
                    }
                    device.setAppType(AppTypeEnum.DDM.getAppType());
                }

                deviceList.addAll(devices);
            }

        }
        QueryWrapper<Devices> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Devices> devices = list(queryWrapper);
        for (Devices device : devices) {
            Device findCamDevice = new Device();
            BeanUtils.copyProperties(device, findCamDevice);
            if (null != findCamDevice.getLogo()) {
                findCamDevice.setCoverUrl(request.getScheme() + "://" + request.getServerName()
                        + ":" + request.getServerPort() + "/api/v1/devices/cover/download/" + findCamDevice.getLogo());
            }
            findCamDevice.setAppType(AppTypeEnum.FIND_CAM.getAppType());
            deviceList.add(findCamDevice);
        }
        response.setList(deviceList);
        response.setCount(deviceList.size());
        return ResponseUtil.getListBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse deviceUpdate(DeviceUpdateParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        //判断设备是不是自己的
        QueryWrapper<Devices> devicesQueryWrapper = new QueryWrapper<>();
        devicesQueryWrapper.eq("sn", param.getSn());
        Devices devices = getOne(devicesQueryWrapper);
        if (devices != null) {
            BeanUtils.copyProperties(param, devices);
            updateById(devices);
        } else {

            if (null != param.getAppType() && param.getAppType().equals(AppTypeEnum.DDM.getAppType())) {
                QueryWrapper<App> appQueryWrapper = new QueryWrapper<>();
                appQueryWrapper.eq("app_type", param.getAppType());
                App app = appMapper.selectOne(appQueryWrapper);
                return deviceUtil.updateDevice(app, param.getSn(), param.getDevName(),
                        String.valueOf(redisUtil.hget("mn" + request.getHeader("access_token"),
                                String.valueOf(app.getAppType()))));

            }

        }
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse deviceBindToUser(DeviceBindToUserParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        Long firmId = (Long) request.getAttribute(FirmKeySecretIntercepter.REQ_ATTR_FIRM_ID);
        QueryWrapper<Devices> queryWrapper = new QueryWrapper<>();
        if (null == param.getAppType()) {
            param.setAppType(getAppType(param.getSn()));
        }
        if (param.getAppType().equals(AppTypeEnum.FIND_CAM.getAppType())) {
            queryWrapper.eq("sn", param.getSn()).eq("firm_id", firmId);
            Devices device = getOne(queryWrapper);
            if (device == null) {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVIDE_INVLID_SN);
            }
            if (device.getUserId() != null) {
                if (device.getUserId().equals(userId)) {
                    return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
                }
                return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVICE_BIND_TO_ANOTHER);
            } else {
                device.setUserId(userId);
                device.setBindTime(new Date());
                updateById(device);
            }
        } else if (param.getAppType().equals(AppTypeEnum.DDM.getAppType())) {
            QueryWrapper<App> appQueryWrapper = new QueryWrapper<>();
            appQueryWrapper.eq("app_type", param.getAppType());
            App app = appMapper.selectOne(appQueryWrapper);
            BaseResponse baseResponse = deviceUtil.bindDevice(app, param.getSn(), param.getVn(),
                    String.valueOf(redisUtil.hget("mn" + request.getHeader("access_token"),
                            String.valueOf(app.getAppType()))));
            if (baseResponse.getCode().equals(ResponseEnum.SUCCESS.getCode())) {
                Devices devices = new Devices();
                devices.setUserId(userId);
                devices.setAddTime(new Date());
                devices.setBindTime(new Date());
                devices.setFirmId(app.getFirmId());
                save(devices);
            }
        } else {
            return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVIDE_INVLID_SN);
        }
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    private Integer getAppType(String sn) {
        QueryWrapper<Devices> devicesQueryWrapper = new QueryWrapper<>();
        devicesQueryWrapper.eq("sn", sn);
        Devices devices = getOne(devicesQueryWrapper);
        if (devices != null) {
            return AppTypeEnum.FIND_CAM.getAppType();
        } else {
            return AppTypeEnum.DDM.getAppType();
        }

    }

    @Override
    public BaseResponse deviceUnbind(DeviceBindToUserParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        Long firmId = (Long) request.getAttribute(FirmKeySecretIntercepter.REQ_ATTR_FIRM_ID);
        if (null == param.getAppType()) {
            param.setAppType(getAppType(param.getSn()));
        }
        QueryWrapper<Devices> queryWrapper = new QueryWrapper<>();
        if (param.getAppType() != null && param.getAppType().equals(AppTypeEnum.FIND_CAM.getAppType())) {
            queryWrapper.eq("sn", param.getSn()).eq("firm_id", firmId);
            Devices device = getOne(queryWrapper);
            if (device == null || device.getUserId() == null || !device.getUserId().equals(userId)) {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVIDE_INVLID_SN);
            } else {
                device.setUserId(null);
                updateById(device);
                return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
            }

        } else if (param.getAppType() != null && param.getAppType().equals(AppTypeEnum.DDM.getAppType())) {
            QueryWrapper<App> appQueryWrapper = new QueryWrapper<>();
            appQueryWrapper.eq("app_type", param.getAppType());
            App app = appMapper.selectOne(appQueryWrapper);
            return deviceUtil.unbindDevice(app, param.getSn(),
                    String.valueOf(redisUtil.hget("mn" + request.getHeader("access_token"),
                            String.valueOf(app.getAppType()))));
        } else {
            return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVIDE_INVLID_SN);
        }

    }

    @Override
    public BaseResponse<SnDeviceInfo> getDeviceInfoFormSn(SingleDeviceParam param, HttpServletRequest request) {
        BaseResponse<SnDeviceInfo> response = new BaseResponse<>();
        SnDeviceInfo snDeviceInfo = new SnDeviceInfo();
        snDeviceInfo.setSn(param.getSn());
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        Long firmId = (Long) request.getAttribute(FirmKeySecretIntercepter.REQ_ATTR_FIRM_ID);
        //判断是什么app_type；
        if (null == param.getAppType()) {
            param.setAppType(getAppType(param.getSn()));
        }
        //不用考虑设备的归属问题
        if (param.getAppType() != null && param.getAppType().equals(AppTypeEnum.FIND_CAM.getAppType())) {
            QueryWrapper<Devices> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("sn", param.getSn());
            Devices device = getOne(queryWrapper);
            if (device == null) {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVIDE_INVLID_SN);
            }
            set(snDeviceInfo, userId, device);
            response.setData(snDeviceInfo);

        } else if (param.getAppType() != null && param.getAppType().equals(AppTypeEnum.DDM.getAppType())) {
            QueryWrapper<App> appQueryWrapper = new QueryWrapper<>();
            appQueryWrapper.eq("app_type", param.getAppType());
            App app = appMapper.selectOne(appQueryWrapper);
            MnDeviceInfoForSn infoForSn = deviceUtil.getDeviceInfoFormSn(app, param.getSn(),
                    String.valueOf(redisUtil.hget("mn" + request.getHeader("access_token"),
                            String.valueOf(app.getAppType()))));
            if (infoForSn.getCode().equals(2000)) {
                BeanUtils.copyProperties(infoForSn, snDeviceInfo);
                response.setData(snDeviceInfo);
            } else {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVIDE_INVLID_SN);
            }
        } else {
            return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVIDE_INVLID_SN);
        }
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse uploadDeviceImg(MultipartFile file, String sn, Integer appType, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        Long firmId = (Long) request.getAttribute(FirmKeySecretIntercepter.REQ_ATTR_FIRM_ID);
        //判断是什么app_type；
        if (null == appType) {
            appType = getAppType(sn);
        }
        if (appType.equals(AppTypeEnum.FIND_CAM.getAppType())) {
            QueryWrapper<Devices> devicesQueryWrapper = new QueryWrapper<>();
            devicesQueryWrapper.eq("sn", sn);
            devicesQueryWrapper.eq("firm_id", firmId);
            Devices devices = getOne(devicesQueryWrapper);
            if (!devices.getUserId().equals(userId)) {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVIDE_INVLID_SN);
            }
            upload(file, devices);

        } else {
            QueryWrapper<App> appQueryWrapper = new QueryWrapper<>();
            appQueryWrapper.eq("app_type", appType);
            App app = appMapper.selectOne(appQueryWrapper);
            QueryWrapper<Devices> devicesQueryWrapper = new QueryWrapper<>();
            devicesQueryWrapper.eq("sn", sn);
            devicesQueryWrapper.eq("firm_id", app.getFirmId());
            Devices devices = getOne(devicesQueryWrapper);
            if (!devices.getUserId().equals(userId)) {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.DEVIDE_INVLID_SN);
            }
            upload(file, devices);

        }
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    private void upload(MultipartFile file, Devices devices) {
        try (InputStream in = file.getInputStream()) {
            String md5 = Md5Util.md5(file.getBytes());
            PutObjectResult result = ossClient.putObject(OssBucketContent.bucketName,
                    "device/" + md5 + "/" + devices.getId(), in);
            if (result != null) {
                devices.setLogo("/" + md5 + "/" + devices.getSn());
            }
            updateById(devices);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downloadDeviceImg(String md5, String sn, HttpServletRequest request,
                                  HttpServletResponse response) {
        Long firmId = (Long) request.getAttribute(FirmKeySecretIntercepter.REQ_ATTR_FIRM_ID);
        QueryWrapper<Devices> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sn", sn);
        Devices device = getOne(queryWrapper);
        if (device == null) {
            return;
        }
        OSSObject ossObject = ossClient.getObject(OssBucketContent.bucketName,
                "device/" + md5 + "/" + device.getId());
        if (ossObject == null) {
            return;
        }
        InputStream in = ossObject.getObjectContent();
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             ServletOutputStream responseOutputStream = response.getOutputStream()) {
            //没有头像
            if (in == null) {
                response.setStatus(404);
                return;
            }
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            byte[] car = new byte[1024];
            int len;
            while (-1 != (len = in.read(car))) {
                os.write(car, 0, len);
            }
            byte[] images = os.toByteArray();
            responseOutputStream.write(images);
            responseOutputStream.flush();
        } catch (Exception e) {
            log.info("[download device——cover]:{}", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {

                }
            }
        }

    }

    private void set(SnDeviceInfo snDeviceInfo, Long userId, Devices device) {
        if (device.getUserId() != null) {
            snDeviceInfo.setBindState(device.getUserId().equals(userId) ? 1 : 2);
            BindUser bindUser = new BindUser();
            Users users = usersMapper.selectById(device.getId());
            bindUser.setPhone(users.getPhone().replace(users.getPhone().substring(4, 7), "****"));
            snDeviceInfo.setBindUser(bindUser);
        } else {
            snDeviceInfo.setBindState(0);
        }
    }

}
