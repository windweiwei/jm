package com.jimang.services;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jimang.model.Users;
import com.jimang.request.*;
import com.jimang.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author jw
 * @since 2020-07-19
 */
public interface UsersService extends IService<Users> {
    /**
     * 用户注册发送验证码
     *
     * @param param   param
     * @param request request
     * @return code 2000 发送验证码
     */
    BaseResponse userSingUpAuthCode(SignUpAuthCodeParam param, HttpServletRequest request);

    /**
     * 接受验证码并注册
     *
     * @param param   。。
     * @param request 。。
     * @return 。。
     */
    BaseResponse userSignUpAndSave(SignUpSaveUserParam param, HttpServletRequest request);

    /**
     * 用户登陆
     *
     * @param param   param
     * @param request request
     * @return 。。
     */
    BaseResponse userLogin(UserLoginParam param, HttpServletRequest request);

    /**
     * 用户信息获取
     *
     * @param request request
     * @return 。。
     */
    BaseResponse<Users> userInfoGet(HttpServletRequest request);

    /**
     * 用户忘记密码
     *
     * @return 。。
     */
    BaseResponse forgotPasswordAuthCode(UserForgotPasswordParam param, HttpServletRequest request);

    /**
     * 忘记密码
     *
     * @param param   param
     * @param request request
     * @return 。。
     */
    BaseResponse forgotPasswprdUpdate(UserForgotPasswordUpdateParam param, HttpServletRequest request);


}
