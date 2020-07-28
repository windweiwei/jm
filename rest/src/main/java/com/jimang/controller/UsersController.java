package com.jimang.controller;


import com.github.fashionbrot.validated.annotation.Validated;
import com.jimang.model.LoginInfo;
import com.jimang.model.Users;
import com.jimang.request.*;
import com.jimang.response.BaseResponse;
import com.jimang.services.UsersService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @Validated
    @ApiOperation(value = "修改密码发送验证码", position = 7)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @PostMapping("/change_password/authcode")
    public BaseResponse changePasswordAuthCode(HttpServletRequest request) {
        return usersService.changePasswordAuthCode(request);

    }

    @Validated
    @ApiOperation(value = "修改密码", position = 8)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @PostMapping("/change_password/change")
    public BaseResponse changePasswordSave(@RequestBody UserChangePasswordParam param, HttpServletRequest request) {
        return usersService.changePasswordSave(param, request);
    }

    @Validated
    @ApiOperation(value = "上传头像", position = 9)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @ApiImplicitParams({@ApiImplicitParam(name = "file", value = "头像文件", required = false,
            paramType = "body", dataType = "file"),
            @ApiImplicitParam(name = "url", value = "头像url",
                    required = false, paramType = "body", dataType = "String")})
    @PostMapping("/head_portrait/upload")
    public BaseResponse headPortraitUpload(@RequestParam(required = false) MultipartFile file,
                                           @RequestParam(required = false) String url,
                                           HttpServletRequest request) {
        FileUploadParam param = new FileUploadParam();
        param.setFile(file);
        param.setUrl(url);
        return usersService.headPortraitUpload(param, request);
    }

    @Validated
    @ApiOperation(value = "头像下载", position = 10)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @ApiImplicitParams({@ApiImplicitParam(name = "md5", value = "md5", required = false,
            paramType = "path", dataType = "String"),
            @ApiImplicitParam(name = "file_name", value = "头像url",
                    required = false, paramType = "path", dataType = "String")})

    @GetMapping("/head_portrait/download/{md5}/{file_name}")
    public void headPortaitUpload(@PathVariable String md5,
                                  @PathVariable(value = "file_name") String fileName,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        usersService.headPortraitDownload(md5, fileName, request, response);

    }

    @Validated
    @ApiOperation(value = "用户信息修改(暂时只能修改昵称)", position = 11)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @PostMapping("/info/update")
    public BaseResponse userInfoUpdate(@RequestBody UserInfoUpdateParam param, HttpServletRequest request) {
        return usersService.userInfoUpdate(param, request);

    }

    @Validated
    @ApiOperation(value = "用户登出", position = 12)
    @ApiResponses({@ApiResponse(code = 500, message = "服务器内部错误", response = BaseResponse.class)})
    @PostMapping("/logout")
    public BaseResponse userSignOut(HttpServletRequest request) {
        return usersService.userSignOut(request);
    }


}
