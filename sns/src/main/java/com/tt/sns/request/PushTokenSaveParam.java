package com.tt.sns.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by wind on 2020/7/8.
 */
@ApiModel
@Data
public class PushTokenSaveParam {
    /**
     * 注册的push——token
     */
    @ApiModelProperty(value = "推送注册当token")
    private String token;
    /**
     * 推送类型 1：小米 2：极光
     */
    @ApiModelProperty(value = "推送平台的type 1:极光 2：华为 3：小米 4：FCM")
    private Integer pushType;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String uid;

    @JsonProperty("time_utc")
    @ApiModelProperty(name = "time_utc", value = "报警当前时间")
    private Long timeUtc;
    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型")
    private Integer type;

}
