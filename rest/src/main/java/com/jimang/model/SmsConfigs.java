package com.jimang.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @since 2020-07-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sms_configs")
@ApiModel(value = "SmsConfigs对象", description = "")
public class SmsConfigs implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("firm_id")
    @JsonProperty("firm_id")
    private Long firmId;

    @TableField("sign_name")
    @JsonProperty("sign_name")
    private String signName;

    @TableField("template_code")
    @JsonProperty("template_code")
    private String templateCode;

    @TableField("ctime")
    @JsonIgnore
    private Date ctime;

    @TableField("mtime")
    @JsonIgnore
    private Date mtime;


    public static final String ID = "id";

    public static final String FIRM_ID = "firm_id";

    public static final String SIGN_NAME = "sign_name";

    public static final String TEMPLATE_CODE = "template_code";

    public static final String CTIME = "ctime";

    public static final String MTIME = "mtime";

}
