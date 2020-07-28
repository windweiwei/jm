package com.jimang.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author jw
 * @since 2020-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("firms")
@ApiModel(value="Firms对象", description="")
public class Firms implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("firm_name")
    @JsonProperty("firm_name")
    private String firmName;

    @TableField("app_key")
    @JsonProperty("app_key")
    private String appKey;

    @TableField("app_secret")
    @JsonProperty("app_secret")
    private String appSecret;

    @TableField("super_id")
    @JsonProperty("user_id")
    private Long superId;

    @TableField("deleted")
    @JsonIgnore
    private Integer deleted;


    public static final String ID = "id";

    public static final String FIRM_NAME = "firm_name";

    public static final String APP_KEY = "app_key";

    public static final String APP_SECRET = "app_secret";

    public static final String SUPER_ID = "super_id";

    public static final String DELETED = "deleted";

}
