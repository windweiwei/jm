package com.tt.sns.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by wind on 2020/7/8.
 */
@ApiModel
@Data
public class Alarm {
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String msg;
    /**
     * token
     */
    @ApiModelProperty(value = "推送token")
    private String token;
    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id")
    private String uid;
    /**
     * 图片url
     */
    @ApiModelProperty(value = "推送图片url")
    @JsonProperty("image_url")
    private String imageUrl;
    /**
     * 报警时间
     */
    @ApiModelProperty(value = "报警时间")
    private Long timeUtc;

    @ApiModelProperty(name = "app_version", value = "app版本")
    @JsonProperty("app_version")
    private String appVersion;
}
