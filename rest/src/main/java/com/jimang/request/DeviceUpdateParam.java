package com.jimang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther:wind
 * @Date:2020/7/28
 * @Version 1.0
 */
@Data
@ApiModel
public class DeviceUpdateParam {
    @ApiModelProperty(value = "设备的id")
    private Long deviceId;
    @ApiModelProperty(value = "设备名称")
    private String devName;
}
