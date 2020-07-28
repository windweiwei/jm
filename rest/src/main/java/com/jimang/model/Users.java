package com.jimang.model;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author jw
 * @since 2020-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("users")
@ApiModel(value = "Users对象", description = "")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonProperty("user_id")
    private Long id;

    @ApiModelProperty(value = "用户名")
    @TableField("user_name")
    @JsonProperty("user_name")
    private String userName;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "昵称")
    @TableField("nike_name")
    @JsonProperty("nick_name")
    private String nikeName;

    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    @TableField("email")
    private String email;

    @ApiModelProperty(value = "头像")
    @TableField("head_portrait")
    @JsonProperty("head_portrait")
    private String headPortrait;

    @ApiModelProperty(value = "登陆时间")
    @TableField("last_login_time")
    @JsonProperty("last_login_time")
    private Date lastLoginTime;

    @ApiModelProperty(value = "国家")
    @TableField("country")
    private String country;

    @ApiModelProperty(value = "省")
    @TableField("province")
    private String province;

    @ApiModelProperty(value = "城市")
    @TableField("city")
    private String city;

    @ApiModelProperty(value = "注册时间")
    @TableField("registration_time")
    @JsonProperty("registration_time")
    private Date registrationTime;

    @ApiModelProperty(value = "蛮牛id")
    @TableField("third_user_id")
    @JsonIgnore
    private Long thirdUserId;

    @ApiModelProperty(value = "修改时间")
    @TableField("mtime")
    @JsonIgnore
    private Date mtime;

    @ApiModelProperty(value = "新增时间")
    @TableField("ctime")
    @JsonIgnore
    private Date ctime;
    @JsonIgnore
    @TableLogic
    @TableField("deleted")
    private Integer deleted;


    public static final String ID = "id";

    public static final String USER_NAME = "user_name";

    public static final String PASSWORD = "password";

    public static final String NIKE_NAME = "nike_name";

    public static final String PHONE = "phone";

    public static final String EMAIL = "email";

    public static final String HEAD_PORTRAIT = "head_portrait";

    public static final String LAST_LOGIN_TIME = "last_login_time";

    public static final String COUNTRY = "country";

    public static final String PROVINCE = "province";

    public static final String CITY = "city";

    public static final String REGISTRATION_TIME = "registration_time";

    public static final String THIRD_USER_ID = "third_user_id";

    public static final String DESC = "desc";

    public static final String MTIME = "mtime";

    public static final String CTIME = "ctime";

}
