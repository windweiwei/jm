package com.jimang.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

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
 * @since 2020-07-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("devices")
@ApiModel(value = "Devices对象", description = "")
public class Devices implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Id主键 ")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "设备名称")
    @TableField("dev_name")
    private String devName;

    @ApiModelProperty(value = "设备sn findCam 的uid")
    @TableField("dev_sn")
    private String devSn;

    @ApiModelProperty(value = "0:离线 1:在线 2:休眠")
    @TableField("state")
    private String state;

    @ApiModelProperty(value = "设备类型")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "设备固件模型")
    @TableField("model")
    private String model;

    @ApiModelProperty(value = "设备密码")
    @TableField("vn")
    private String vn;

    @ApiModelProperty(value = "固件版本")
    @TableField("version")
    private String version;

    @ApiModelProperty(value = "通道数")
    @TableField("channel")
    private Integer channel;

    @ApiModelProperty(value = "厂商id")
    @TableField("firm_id")
    @JsonProperty("firm_id")
    private Long firmId;

    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    @JsonProperty("user_id")
    private Long userId;

    @ApiModelProperty(value = "添加时间")
    @TableField("add_time")
    @JsonProperty("add_time")
    private LocalDateTime addTime;

    @ApiModelProperty(value = "绑定时间")
    @TableField("bind_time")
    @JsonProperty("bind_time")
    private LocalDateTime bindTime;

    @ApiModelProperty(value = "第一次时间")
    @TableField("first_online_time")
    @JsonProperty("first_online_time")
    private LocalDateTime firstOnlineTime;

    @ApiModelProperty(value = "上一次离线时间")
    @TableField("last_off_time")
    @JsonProperty("last_off_time")
    private LocalDateTime lastOffTime;

    @ApiModelProperty(value = "图片报警存储时间")
    @TableField("img_alarm_days")
    @JsonProperty("img_alarm_days")
    private Integer imgAlarmDays;

    @ApiModelProperty(value = "设备用户名")
    @TableField("admin_name")
    @JsonProperty("admin_name")
    private String adminName;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    @JsonProperty("password")
    private String password;


}
