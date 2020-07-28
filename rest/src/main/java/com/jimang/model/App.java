package com.jimang.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/21
 * @Version 1.0
 */
@TableName("apps")
@ApiModel
@Data
public class App {
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("app_key")
    @ApiModelProperty(value = "app_key")
    @JsonProperty("app_key")
    private String appKey;

    @TableField("app_secret")
    @ApiModelProperty(value = "app_secret")
    @JsonProperty("app_secret")
    private String appSecret;

    @TableField("app_name")
    @ApiModelProperty(value = "app_name")
    @JsonProperty("app_name")
    private String appName;

    @TableField("firm_id")
    @ApiModelProperty(value = "firm_id")
    @JsonProperty("firm_id")
    private Long firmId;

    @TableField("login_api")
    @ApiModelProperty(value = "login_api")
    @JsonProperty("login_api")
    private String loginApi;

    @TableField("admin_name")
    @ApiModelProperty(value = "admin_name")
    @JsonProperty("admin_name")
    private String adminName;

    @TableField("admin_password")
    @ApiModelProperty(value = "admin_password")
    @JsonProperty("admin_password")
    private String adminPassword;

    @TableField("admin_login_api")
    @ApiModelProperty(value = "admin_login_api")
    @JsonProperty("admin_login_api")
    private String adminLoginApi;

    @TableField("create_user_api")
    @ApiModelProperty(value = "create_user_api")
    @JsonProperty("create_user_api")
    private String createUserApi;


}
