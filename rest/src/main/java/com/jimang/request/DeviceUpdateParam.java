package com.jimang.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/28
 * @Version 1.0
 */
@Data
@ApiModel
public class DeviceUpdateParam {
    @ApiModelProperty(value = "设备的sn")
    @JsonProperty("sn")
    private String sn;
    @ApiModelProperty(value = "设备名称")
    @JsonProperty("dev_name")
    private String devName;
    @JsonProperty("admin_name")
    @ApiModelProperty(value = "设备用户名 默认是admin")
    private String adminName;
    @ApiModelProperty(value = "设备密码 默认是admin")
    private String password;
    @ApiModelProperty("app类型")
    @JsonProperty("app_type")
    private Integer appType;
}
