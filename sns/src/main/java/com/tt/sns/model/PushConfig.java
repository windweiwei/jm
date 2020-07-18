package com.tt.sns.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.Date;

/**
 * Created by wei on 2020/7/8.
 */
@Data
@ApiModel
public class PushConfig {
    @TableId
    private Long id;

    @JsonProperty("push_client_name")
    @TableField("push_client_name")
    @ApiModelProperty(name = "push_client_name", value = "推送平台名称")
    private String pushClientName;
    /**
     * 当为FCM推送类型时，apiKey值为json路径
     */
    @JsonProperty("api_key")
    @TableField("api_key")
    @ApiModelProperty(name = "api_key", value = "推送平台的key")
    private String apiKey;

    @JsonProperty("firm_id")
    @TableField("firm_id")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(name = "firm_id", value = "厂商id")
    private Long firmId;

    /**
     * 当为FCM推送类型时，apiSecret值为databaseURL
     */
    @JsonProperty("api_secret")
    @TableField("api_secret")
    @ApiModelProperty(name = "api_secret", value = "平台的secret")
    private String apiSecret;

    @JsonProperty("pack_name")
    @TableField("pack_name")
    @ApiModelProperty(name = "pack_name", value = "包名称")
    private String packname;


    @JsonProperty("push_type")
    @TableField("push_type")
    @ApiModelProperty(name = "push_type", value = "推送类型")
    private Integer pushType;

    @TableField("resource")
    @ApiModelProperty(name = "resource", value = "资源")
    private String resource;

    @JsonIgnore
    @TableField("ctime")
    private Date ctime;

    @JsonIgnore
    @TableField("mtime")
    private Date mtime;

    @TableLogic
    @JsonIgnore
    private Integer deleted;

}
