package com.jimang.services.impl;


import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.jimang.enums.ResponseEnum;
import com.jimang.intercepter.AcessTokenIntercepter;
import com.jimang.mapper.SmsConfigsMapper;
import com.jimang.mapper.UsersMapper;
import com.jimang.model.LoginInfo;
import com.jimang.model.RandomCode;
import com.jimang.model.SmsConfigs;
import com.jimang.model.Users;
import com.jimang.request.*;
import com.jimang.response.BaseResponse;
import com.jimang.services.UsersService;
import com.jimang.util.AliSmsUtil;
import com.jimang.util.CreateRandomUtil;
import com.jimang.util.RedisUtil;
import com.jimang.util.ResponseUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private SmsConfigsMapper smsConfigsMapper;
    private static final Integer TOKEN_TIME_EXPIRE = 600;
    private static final Integer ACCESS_TOKEN_TIME_EXPTRE = 7200;

    @Override
    public BaseResponse userSingUpAuthCode(SignUpAuthCodeParam param,
                                           HttpServletRequest request) {
        BaseResponse response = new BaseResponse();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        if (param.getPhone() != null) {
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
                users.setUserName(StringUtils.isEmpty(param.getUserName().trim())
                        ? null : param.getUserName());
                System.out.println(encoder.encode(param.getPassword()));
                users.setPassword(encoder.encode(param.getPassword()));
                users.setDeleted(0);
                users.setCtime(new Date());
                users.setCtime(new Date());
                users.setRegistrationTime(new Date());
                save(users);
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
        queryWrapper.or().eq("user_name", param.getUserName()).or()
                .eq("phone", param.getUserName())
                .or().eq("email", param.getUserName());

        Users users = getOne(queryWrapper);
        if (users == null || null == param.getPassword()) {
            return ResponseUtil.getBaseResponse(baseResponse, ResponseEnum.USER_NAME_OR_PASSWORD_ERROR);
        }
        Boolean it = BCrypt.checkpw(param.getPassword(), users.getPassword());
        if (!it) {
            ResponseUtil.getBaseResponse(baseResponse, ResponseEnum.USER_NAME_OR_PASSWORD_ERROR);
        }
        LoginInfo info = new LoginInfo();
        String token = "u_" + UUID.randomUUID();
        redisUtil.set(token, users.getId(), ACCESS_TOKEN_TIME_EXPTRE);
        info.setUserId(users.getId());
        info.setAccessToken(token);
        info.setUserName(users.getUserName());
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

}
