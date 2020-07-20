package com.jimang.controller;


import com.github.fashionbrot.validated.annotation.Validated;
import com.jimang.model.LoginInfo;
import com.jimang.model.Users;
import com.jimang.request.*;
import com.jimang.response.BaseResponse;
import com.jimang.services.UsersService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jw
 * @since 2020-07-19
 */
@RestController
@RequestMapping("/api/v1/users")
@Api("用户模块")
public class UsersController {
    @Autowired
    private UsersService usersService;

    @Validated
    @ApiOperation(value = "用户注册第一步发送验证码", position = 1)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @PostMapping("/auth_code")
    public BaseResponse userSingUpAuthCode(@RequestBody SignUpAuthCodeParam param, HttpServletRequest request) {

        return usersService.userSingUpAuthCode(param, request);
    }

    @Validated
    @ApiOperation(value = "用户注册第二步 保存用户", position = 2)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @PostMapping("/save")
    public BaseResponse userSignUpAndSave(@RequestBody SignUpSaveUserParam param, HttpServletRequest request) {
        return usersService.userSignUpAndSave(param, request);
    }

    @Validated
    @ApiOperation(value = "用户登陆", position = 3)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @PostMapping("/login")
    public BaseResponse<LoginInfo> userLogin(@RequestBody UserLoginParam param, HttpServletRequest request) {
        return usersService.userLogin(param, request);
    }

    @Validated
    @ApiOperation(value = "获取用户信息", position = 4)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @GetMapping("/info")
    public BaseResponse<Users> userInfoGet(HttpServletRequest request) {
        return usersService.userInfoGet(request);
    }

    @Validated
    @ApiOperation(value = "忘记密码发送验证码", position = 5)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @PostMapping("/forgot_password/auth_code")
    public BaseResponse forgotPasswordAuthCode(@RequestBody UserForgotPasswordParam param,
                                               HttpServletRequest request) {
        return usersService.forgotPasswordAuthCode(param, request);

    }

    @Validated
    @ApiOperation(value = "忘记密码修改密码", position = 6)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @PostMapping("/forgot_password/update")
    public BaseResponse forgotPasswprdUpdate(@RequestBody UserForgotPasswordUpdateParam param,
                                             HttpServletRequest request) {
        return usersService.forgotPasswprdUpdate(param, request);
    }


}
