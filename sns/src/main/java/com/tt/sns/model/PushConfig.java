package com.tt.sns.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
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
    private String pushClientName;
    /**
     * 当为FCM推送类型时，apiKey值为json路径
     */
    @JsonProperty("api_key")
    @TableField("api_key")
    private String apiKey;

    @JsonProperty("firm_id")
    @TableField("firm_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long firmId;

    /**
     * 当为FCM推送类型时，apiSecret值为databaseURL
     */
    @JsonProperty("api_secret")
    @TableField("api_secret")
    private String apiSecret;

    @JsonProperty("pack_name")
    @TableField("pack_name")
    private String packname;

    @JsonProperty("push_type")
    @TableField("push_type")
    private Integer pushType;

    @TableField("resource")
    private String resource;

    @TableField("ctime")
    private Date ctime;

    @TableField("mtime")
    private Date mtime;
    @TableLogic
    @JsonIgnore
    private Integer deleted;

}
