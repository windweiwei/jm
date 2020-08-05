package com.jimang.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import net.sf.json.JSONObject;


import java.util.Date;
import java.util.List;

/**
 * @Auther:wind
 * @Date:2020/7/27
 * @Version 1.0
 */
@Data
@ApiModel
public class Device {

    private String id;

    @JsonProperty("app_type")
    private Integer appType;
    /**
     * 设备sn
     */
    @ApiModelProperty(value = "sn")
    private String sn;
    /**
     * 设备名称
     */
    @JsonProperty("dev_name")
    @ApiModelProperty(value = "dev_name")
    private String devName;
    /**
     * ip
     */
    @ApiModelProperty(value = "设备ip")
    private String ip;
    /**
     * 设备端口
     */
    @ApiModelProperty("设备端口")
    private Integer port;
    /**
     * 设备类型
     */
    @ApiModelProperty("设备类型")
    private Integer type;
    /**
     * 固件版本
     */
    @ApiModelProperty("固件类型")
    private String ver;
    /**
     * 固件型号
     */
    @ApiModelProperty("固件型号")
    private String model;
    /**
     * 设备密码
     */
    @ApiModelProperty("设备密码")
    private String vn;
    /**
     * 设备封面，已经被cover_url 代替 但是因为要兼容所以暂时不删除
     */
    @ApiModelProperty("设备封面")
    private String logo;
    /**
     * 封面类型
     */
    @JsonProperty("logo_type")
    @ApiModelProperty("封面类型")
    private Integer logoType;
    /**
     * 添加时间
     */
    @JsonProperty("add_time")
    @ApiModelProperty("添加时间")
    private Long addTime;

    /**
     * 厂商id
     */
    @JsonProperty("manufacturer_id")
    @ApiModelProperty(value = "厂商id")
    private Long manufacturerId;
    /**
     * 通道数
     */
    @ApiModelProperty("通道数")
    private Integer channels;

    @JsonProperty("ctrl_access")
    private Integer ctrlAccess;
    /**
     * 第一次上线时间
     */
    @JsonProperty("first_online_time")
    @ApiModelProperty(value = "第一次上线时间")
    private long firstOnlineTime;
    /**
     * 报警存储天数
     */
    @JsonProperty("alarm_storage_days")
    @ApiModelProperty("报警存储天数")
    private String alarmStorageDays;
    /**
     * 经度
     */
    @ApiModelProperty(value = "经度")
    private Float longitude;
    /**
     * 纬度
     */
    @ApiModelProperty("纬度")
    private Float latitude;
    /**
     * 在线状态
     */
    @ApiModelProperty("在线状态")
    private Integer online;
    /**
     * 人脸库id
     */
    @ApiModelProperty("人脸库id")
    private String facelib;
    /**
     * 权限
     */
    @ApiModelProperty("权限")
    private Integer authority;
    /**
     * 来自
     */
    @ApiModelProperty(value = "来自谁度谁的分享")
    private String from;

    @JsonProperty("last_offline_time")
    @ApiModelProperty("最后一次离线时间")
    private String lastOfflineTime;

    @ApiModelProperty(value = "绑定时间")
    private Date bindTime;
    /**
     * 剩余时间
     */
    @JsonProperty("remain_time")
    @ApiModelProperty("剩余时间")
    private Integer remainTime;

    @JsonProperty("cover_url")
    @ApiModelProperty("封面Url")
    private String coverUrl;


    /**
     * 能力值Json
     */
    @JsonProperty("support_ability")
    @ApiModelProperty(value = "能力值json")
    private JSONObject supportAbility;
    /**
     * 存储类型
     */
    @JsonProperty("storage_type")
    private Integer storageType;
    /**
     * 是否领取过免费套餐 0 ：未领取 1：领取过
     */
    @JsonProperty("storage_received")
    private Integer storageReceived;
    /**
     * 工作模式状态 0：关闭；1：开启
     */
    @JsonProperty("dev_work_state")
    private Integer devWorkState;
    /**
     * 固件型号别名
     */
    @JsonProperty("model_alias")
    private String modelAlias;

    /**
     * 是否加密
     */
    private Integer encryption;
    /**
     * 如果是4G 设备就会有这个值
     */
    private String iccid;
    /**
     * 设备分享用户
     */
    private List<ShareUser> users;

    @JsonProperty("channel_images")
    private List<ChannelImages> channelImages;
    /**
     * 电池电量
     */
    private List<BatteryItem> battery;
}