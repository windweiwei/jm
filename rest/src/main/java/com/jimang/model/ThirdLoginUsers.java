package com.jimang.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
 * @since 2020-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("third_login_users")
@ApiModel(value = "ThirdLoginUsers对象", description = "")
public class ThirdLoginUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "用户名")
    @TableField("user_name")
    @JsonProperty("user_name")
    private String userName;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "登陆方式")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "app_id")
    @TableField("app_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long appId;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;


    public static final String ID = "id";

    public static final String USER_NAME = "user_name";

    public static final String PASSWORD = "password";

    public static final String TYPE = "type";

    public static final String APP_ID = "app_id";

}
