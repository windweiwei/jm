package com.jimang.services.impl;


import com.aliyun.oss.OSS;
import com.aliyun.oss.model.OSSObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jimang.content.OssBucketContent;
import com.jimang.enums.ResponseEnum;
import com.jimang.intercepter.AcessTokenIntercepter;
import com.jimang.mapper.AppMapper;
import com.jimang.mapper.SmsConfigsMapper;
import com.jimang.mapper.ThirdLoginUsersMapper;
import com.jimang.mapper.UsersMapper;
import com.jimang.model.*;
import com.jimang.request.*;
import com.jimang.response.BaseResponse;
import com.jimang.response.MnLoginResponse;
import com.jimang.services.UsersService;
import com.jimang.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.jimang.enums.ResponseEnum.USERS_FORGOT_PASSWORD_ACTINE_CODE_ERROR;

/**
 * <p>
 * 用户模块
 * </p>
 *
 * @author jw
 * @since 2020-07-19
 */
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SmsConfigsMapper smsConfigsMapper;
    @Autowired
    private ThirdLoginUsersMapper thirdLoginUsersMapper;
    @Autowired
    private UserUtil userUtil;
    @Autowired
    AppMapper appMapper;
    @Autowired
    private OSS ossClient;
    private static final Integer TOKEN_TIME_EXPIRE = 600;
    private static final Integer ACCESS_TOKEN_TIME_EXPTRE = 7200;

    @Override
    public BaseResponse userSingUpAuthCode(SignUpAuthCodeParam param,
                                           HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        if (param.getPhone() != null) {
            if (!ValidationUtil.isPhone(param.getPhone())) {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.INVALID_PARAMS);
            }
            queryWrapper.eq("phone", param.getPhone());
            Users users = getOne(queryWrapper);
            if (null == users) {
                QueryWrapper<SmsConfigs> configsQueryWrapper = new QueryWrapper<>();
                configsQueryWrapper.eq("firm_id", 1L);
                configsQueryWrapper.eq("type", 1);
                SmsConfigs smsConfigs = smsConfigsMapper.selectOne(configsQueryWrapper);
                RandomCode code = new RandomCode();
                code.setCode(CreateRandomUtil.getSixRandom());
                sendActiveCode(param.getPhone(), smsConfigs, code);
                redisUtil.set("phone_" + param.getPhone(), code.getCode(), TOKEN_TIME_EXPIRE);
            } else {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.USER_PHONE_EXITS);
            }
        }
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse userSignUpAndSave(SignUpSaveUserParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        if (null != param.getPhone()) {
            Integer code = (Integer) redisUtil.get("phone_" + param.getPhone());
            if (null != code) {
                if (!code.equals(param.getAuthCode())) {
                    return ResponseUtil.getBaseResponse(response, ResponseEnum.SIGN_UP_TOKEN_INVALID);
                }
                Users users = new Users();
                BeanUtils.copyProperties(param, users);
                users.setPhone(param.getPhone());
                users.setEmail(null);
                if (null != param.getUserName()) {
                    users.setUserName(StringUtils.isEmpty(param.getUserName().trim())
                            ? null : param.getUserName());
                }
                System.out.println(encoder.encode(param.getPassword()));
                users.setPassword(encoder.encode(param.getPassword()));
                users.setDeleted(0);
                users.setCtime(new Date());
                users.setCtime(new Date());
                users.setRegistrationTime(new Date());
                save(users);
                QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("phone", users.getPhone());
                users = getOne(queryWrapper);
                if (users != null) {
                    QueryWrapper<App> appQueryWrapper = new QueryWrapper<>();
                    List<App> apps = appMapper.selectList(appQueryWrapper);
                    for (App app : apps) {
                        Boolean it = userUtil.createUser(app, users.getPhone(), users.getPassword());
                        if (it) {
                            ThirdLoginUsers thirdLoginUsers = new ThirdLoginUsers();
                            thirdLoginUsers.setAppId(app.getId());
                            thirdLoginUsers.setType(1);
                            thirdLoginUsers.setUserName("users-" + users.getPhone());
                            thirdLoginUsers.setPassword(users.getPassword().substring(0, 10));
                            thirdLoginUsers.setUserId(users.getId());
                            thirdLoginUsersMapper.insert(thirdLoginUsers);
                        }
                    }
                }

            } else {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.SIGN_UP_TOKEN_INVALID);
            }
        }
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse<LoginInfo> userLogin(UserLoginParam param, HttpServletRequest request) {
        BaseResponse<LoginInfo> baseResponse = new BaseResponse<>();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.or().eq("user_name", param.getUserName())
                .or().eq("phone", param.getUserName())
                .or().eq("email", param.getUserName());

        Users users = getOne(queryWrapper);
        if (null == users) {
            return ResponseUtil.getBaseResponse(baseResponse, ResponseEnum.USER_PHONE_OR_EMAIL_EXITS);
        }
        if (null == param.getPassword()) {
            return ResponseUtil.getBaseResponse(baseResponse, ResponseEnum.USER_NAME_OR_PASSWORD_ERROR);
        }
        Boolean it = BCrypt.checkpw(param.getPassword(), users.getPassword());
        if (!it) {
            return ResponseUtil.getBaseResponse(baseResponse, ResponseEnum.USER_NAME_OR_PASSWORD_ERROR);
        }
        users.setLastLoginTime(new Date());
        updateById(users);
        LoginInfo info = new LoginInfo();
        String token = "u_" + UUID.randomUUID();
        redisUtil.set(token, users.getId(), ACCESS_TOKEN_TIME_EXPTRE);
        info.setUserId(users.getId());
        info.setAccessToken(token);
        info.setUserName(users.getUserName());
        QueryWrapper<App> appQueryWrapper = new QueryWrapper<>();
        List<App> apps = appMapper.selectList(appQueryWrapper);
        List<ThirdAppLoginInfo> loginInfos = new ArrayList<>();
        for (App app : apps) {
            QueryWrapper<ThirdLoginUsers> usersQueryWrapper = new QueryWrapper<>();
            usersQueryWrapper.eq("user_id", users.getId());
            List<ThirdLoginUsers> thirdLoginUsers = thirdLoginUsersMapper.selectList(usersQueryWrapper);
            for (ThirdLoginUsers thirdLoginUser : thirdLoginUsers) {
                if (null != thirdLoginUser) {
                    try {
                        MnLoginResponse loginResponse = userUtil.thirdLogin(app.getLoginApi(), app.getAppKey(), app.getAppSecret(),
                                thirdLoginUser.getUserName(), thirdLoginUser.getPassword());
                        if (null != loginResponse && loginResponse.getCode().equals(2000)) {
                            ThirdAppLoginInfo thirdAppLoginInfo = new ThirdAppLoginInfo();
                            thirdAppLoginInfo.setAccessToken(loginResponse.getAccessToken());
                            thirdAppLoginInfo.setAppName(app.getAppName());
                            thirdAppLoginInfo.setIdmToken(loginResponse.getIdmToken());
                            thirdAppLoginInfo.setUserId(loginResponse.getUserId());
                            thirdAppLoginInfo.setAppKey(app.getAppKey());
                            thirdAppLoginInfo.setAppSecret(app.getAppSecret());
                            thirdAppLoginInfo.setFirmId(app.getFirmId());
                            loginInfos.add(thirdAppLoginInfo);
                            redisUtil.sSet("mn" + token, loginResponse.getAccessToken());
                        } else {
                            log.error(JsonMapper.writeValueAsString(loginResponse));
                        }
                    } catch (Exception e) {
                        log.error("e" + e);
                    }
                }
            }

        }
        info.setLoginInfos(loginInfos);
        baseResponse.setData(info);
        return ResponseUtil.getBaseResponse(baseResponse, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse<Users> userInfoGet(HttpServletRequest request) {
        BaseResponse<Users> baseResponse = new BaseResponse<>();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        Users users = getOne(queryWrapper);
        if (users.getHeadPortrait() != null) {
            users.setHeadPortrait(request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + "/api/v1/users/head_portrait/download/" + users.getHeadPortrait());
        }
        if (users == null) {
            return ResponseUtil.getBaseResponse(baseResponse, ResponseEnum.INVALID_ACCESS_TOKEN);
        }
        baseResponse.setData(users);
        return ResponseUtil.getBaseResponse(baseResponse, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse forgotPasswordAuthCode(UserForgotPasswordParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        if (null != param.getPhone()) {
            if (!ValidationUtil.isPhone(param.getPhone())) {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.INVALID_PARAMS);
            }
            QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", param.getPhone());
            Users users = getOne(queryWrapper);
            if (users == null) {
                return ResponseUtil.getBaseResponse(response, ResponseEnum.USERS_PHONE_NO_SIGN_UP);
            }
            QueryWrapper<SmsConfigs> configsQueryWrapper = new QueryWrapper<>();
            configsQueryWrapper.eq("firm_id", 1L);
            configsQueryWrapper.eq("type", 1);
            SmsConfigs smsConfigs = smsConfigsMapper.selectOne(configsQueryWrapper);


            RandomCode code = new RandomCode();
            code.setCode(CreateRandomUtil.getSixRandom());
            sendActiveCode(param.getPhone(), smsConfigs, code);
            redisUtil.set("forgot_password_phone" + param.getPhone(), code.getCode(), TOKEN_TIME_EXPIRE);
        } else if (null != param.getEmail()) {

        }


        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    /**
     * send
     *
     * @param phone      手机号
     * @param smsConfigs smsconfig
     */
    private void sendActiveCode(String phone, SmsConfigs smsConfigs, RandomCode code) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AliSmsUtil
                    .sendSms(phone,
                            smsConfigs.getSignName(),
                            smsConfigs.getTemplateCode(), mapper.writeValueAsString(code));
        } catch (JsonProcessingException e) {
            log.error("e", e);
            e.printStackTrace();
        }
    }

    @Override
    public BaseResponse forgotPasswprdUpdate(UserForgotPasswordUpdateParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        if (null != param.getPhone()) {
            Integer activeCode = (Integer) redisUtil.get("forgot_password_phone" + param.getPhone());
            if (activeCode == null) {
                return ResponseUtil.getBaseResponse(response, USERS_FORGOT_PASSWORD_ACTINE_CODE_ERROR);
            } else {
                if (!param.getActiveCode().equals(activeCode)) {
                    return ResponseUtil.getBaseResponse(response, USERS_FORGOT_PASSWORD_ACTINE_CODE_ERROR);
                }
                QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("phone", param.getPhone());
                Users users = getOne(queryWrapper);
                if (users != null) {
                    users.setPassword(encoder.encode(param.getPassword()));
                    updateById(users);
                }

            }

        } else if (null != param.getEmail()) {

        }

        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse changePasswordAuthCode(HttpServletRequest request) {
        BaseResponse baseResponse = new BaseResponse();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        Users users = getOne(queryWrapper);
        if (null != users) {
            QueryWrapper<SmsConfigs> configsQueryWrapper = new QueryWrapper<>();
            configsQueryWrapper.eq("firm_id", 1L);
            configsQueryWrapper.eq("type", 1);
            SmsConfigs smsConfigs = smsConfigsMapper.selectOne(configsQueryWrapper);
            RandomCode code = new RandomCode();
            code.setCode(CreateRandomUtil.getSixRandom());
            sendActiveCode(users.getPhone(), smsConfigs, code);
            redisUtil.set("change_password" + users.getPhone(), code.getCode(), TOKEN_TIME_EXPIRE);

        }
        return ResponseUtil.getBaseResponse(baseResponse, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse changePasswordSave(UserChangePasswordParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        Users users = getOne(queryWrapper);
        if (null != users.getPhone()) {
            Integer activeCode = (Integer) redisUtil.get("change_password" + users.getPhone());
            if (activeCode != null && activeCode.equals(param.getActiveCode())) {
                users.setPassword(encoder.encode(param.getPassword()));
                updateById(users);
            }
        }
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse headPortraitUpload(FileUploadParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        QueryWrapper<Users> usersQueryWrapper = new QueryWrapper<>();
        usersQueryWrapper.eq("id", userId);
        Users users = getOne(usersQueryWrapper);
        if (null != param.getFile()) {
            try (InputStream is = new ByteArrayInputStream(param.getFile().getBytes());) {
                String md5 = Md5Util.md5(param.getFile().getBytes());
                ossClient.putObject(OssBucketContent.bucketName, md5 + "/" +
                        userId.toString() + param.getFile().getOriginalFilename(), is);
                users.setHeadPortrait(md5 + "/" + userId + param.getFile().getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (null != param.getUrl()) {
            users.setHeadPortrait(param.getUrl());
        } else {
            return ResponseUtil.getBaseResponse(response, ResponseEnum.INVALID_PARAMS);

        }
        updateById(users);
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public void headPortraitDownload(String md5, String fileName, HttpServletRequest request,
                                     HttpServletResponse response) {
        OSSObject ossObject = ossClient.getObject(OssBucketContent.bucketName,
                md5 + "/" + fileName);
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
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
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
            log.info("[download userLogo]:{}", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {

                }
            }
        }
    }

    @Override
    public BaseResponse userInfoUpdate(UserInfoUpdateParam param, HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        Users users = getOne(queryWrapper);
        if (users != null) {
            if (!StringUtils.isEmpty(param.getNickName().trim())) {
                users.setNikeName(param.getNickName());
            }
            updateById(users);

        }
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

    @Override
    public BaseResponse userSignOut(HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        Long userId = (Long) request.getAttribute(AcessTokenIntercepter.REQ_ATTR_USER_ID);
        redisUtil.del((String) request.getHeader("access_token"));
        //todo 删除推送token
        return ResponseUtil.getBaseResponse(response, ResponseEnum.SUCCESS);
    }

}
